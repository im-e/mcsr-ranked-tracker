package com.imi.mcsrapp.service;

import com.imi.mcsrapp.model.*;
import com.imi.mcsrapp.model.mcsrapi.Match;
import com.imi.mcsrapp.model.mcsrapi.User;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class MCSRService {

    private final WebClient webClient;

    public MCSRService(WebClient mcsrWebClient) {
        this.webClient = mcsrWebClient;
    }

    public Mono<User> getUserByIdentifier(String identifier) {
        return webClient.get()
                .uri("/users/{identifier}", identifier)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<APIResponse<User>>() {})
                .map(response -> {
                    User user = response.getData();
                    if (user != null) {
                        user.setRankTier(RankTier.fromElo(user.getEloRate()));
                    }
                    return user;
                });
    }

    public List<Match> getUserMatchesSync(String identifier, Integer count) {
        // Convenience overload: fetch recent matches without type filtering.
        return getUserMatchesSync(identifier, count, null);
    }

    public List<Match> getUserMatchesSync(String identifier, Integer count, Integer matchType) {
        APIResponse<List<Match>> response = webClient.get()
                .uri(uriBuilder -> {
                    org.springframework.web.util.UriBuilder builder = uriBuilder
                            .path("/users/{identifier}/matches")
                            .queryParam("count", count != null ? count : 20);
                    if (matchType != null) {
                        builder = builder.queryParam("type", matchType);
                    }
                    return builder.build(identifier);
                })
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<APIResponse<List<Match>>>() {})
                .block();

        return response != null ? response.getData() : null;
    }

    public MatchStatistics getUserMatchStatistics(String identifier, Integer count, Integer matchType) {
        if (matchType == null) {
            return null;
        }

        List<Match> matches = getUserMatchesSync(identifier, count, matchType);
        if (matches == null || matches.isEmpty()) {
            return null;
        }

        return calculateMatchStatistics(matches, identifier, matchType);
    }

    public UserStatistics getUserStatistics(String identifier, Integer count) {
        UserStatistics stats = new UserStatistics();
        User user = getUserByIdentifierSync(identifier);
        populateBestTimesFromUserStats(stats, user);

        for (int type : new int[]{2, 3}) {
            List<Match> matches = getUserMatchesSync(identifier, count, type);
            if (matches == null || matches.isEmpty()) {
                continue;
            }
            MatchStatistics matchStats = calculateMatchStatistics(matches, identifier, type);
            if (matchStats != null) {
                stats.getMatchStatisticsByType().put(type, matchStats);
            }
        }

        return stats;
    }

    private MatchStatistics calculateMatchStatistics(
            List<Match> matches,
            String identifier,
            Integer matchType) {
        List<Match> matchesByType = matches.stream()
                .filter(m -> matchType.equals(m.getType()))
                .toList();
        if (matchesByType.isEmpty()) {
            return null;
        }

        MatchStatistics stats = new MatchStatistics();

        String userUuid = matchesByType.stream()
                .flatMap(m -> m.getPlayers().stream())
                .filter(p -> p.getNickname().equalsIgnoreCase(identifier)
                        || p.getUuid().equals(identifier))
                .findFirst()
                .map(Match.Player::getUuid)
                .orElse(null);

        if (userUuid == null) {
            return null;
        }

        List<Match> validMatchesAllTypes = matchesByType.stream()
                .filter(m -> !Boolean.TRUE.equals(m.getDecayed()) && m.getResult() != null)
                .toList();

        List<Match> validMatches = validMatchesAllTypes;

        stats.setTotalMatches(matchesByType.size());

        int decayedCount = 0;
        int missingResultCount = 0;
        for (Match match : matchesByType) {
            if (Boolean.TRUE.equals(match.getDecayed())) {
                decayedCount++;
                continue;
            }
            if (match.getResult() == null) {
                missingResultCount++;
            }
        }
        stats.setDecayedMatches(decayedCount);
        stats.setMissingResultMatches(missingResultCount);

        stats.setValidMatches(validMatches.size());
        stats.setBestTimeLastMatches(findBestTime(validMatches, userUuid));
        stats.setAverageTimeLastMatches(findAverageTime(validMatches, userUuid));
        populateEloChange(stats, validMatches, userUuid, matchType);

        for (Match match : validMatches) {
            String resultUuid = match.getResult().getUuid();
            boolean isWin = userUuid.equals(resultUuid);
            boolean isDraw = isDrawResult(resultUuid);
            boolean isForfeit = Boolean.TRUE.equals(match.getForfeited());
            Long time = isWin ? match.getResult().getTime() : null;

            // Overall stats for the filtered match list.
            stats.getOverall().recordMatch(isWin, isDraw, isForfeit, time);

            // Bastion-type breakdown.
            String bastion = match.getBastionType() != null ? match.getBastionType() : "UNKNOWN";
            stats.getBastionStats()
                    .computeIfAbsent(bastion, k -> new StatBucket())
                    .recordMatch(isWin, isDraw, isForfeit, time);

            // Seed-type breakdown.
            String seed = match.getSeedType() != null ? match.getSeedType() : "UNKNOWN";
            stats.getSeedStats()
                    .computeIfAbsent(seed, k -> new StatBucket())
                    .recordMatch(isWin, isDraw, isForfeit, time);
        }

        stats.setBestBastion(findBest(stats.getBastionStats()));
        stats.setWorstBastion(findWorst(stats.getBastionStats()));
        stats.setBestSeed(findBest(stats.getSeedStats()));
        stats.setWorstSeed(findWorst(stats.getSeedStats()));

        return stats;
    }

    private User getUserByIdentifierSync(String identifier) {
        APIResponse<User> response = webClient.get()
                .uri("/users/{identifier}", identifier)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<APIResponse<User>>() {})
                .block();

        return response != null ? response.getData() : null;
    }

    private void populateBestTimesFromUserStats(UserStatistics stats, User user) {
        if (user == null || user.getStatistics() == null) {
            return;
        }

        User.StatsPeriod season = user.getStatistics().getSeason();
        User.StatsPeriod total = user.getStatistics().getTotal();

        stats.setBestTimeSeason(resolveBestTimeRanked(season));
        stats.setAverageTimeSeasonRanked(resolveAverageTimeRanked(season));
        stats.setAverageTimeAllTimeRanked(resolveAverageTimeRanked(total));

        populateBestTimeByType(stats.getBestTimeAllTimeByType(), total);
        populateBestTimeByType(stats.getBestTimeSeasonByType(), season);
        populateAverageTimeByType(stats.getAverageTimeAllTimeByType(), total);
        populateAverageTimeByType(stats.getAverageTimeSeasonByType(), season);
    }

    private Long resolveBestTimeRanked(User.StatsPeriod period) {
        if (period == null || period.getBestTime() == null) {
            return null;
        }

        return period.getBestTime().getRanked();
    }

    private void populateBestTimeByType(Map<Integer, Long> target, User.StatsPeriod period) {
        if (period == null || period.getBestTime() == null) {
            return;
        }

        Long ranked = period.getBestTime().getRanked();
        Long casual = period.getBestTime().getCasual();

        if (casual != null) {
            target.put(1, casual);
        }

        if (ranked != null) {
            target.put(2, ranked);
        }
    }

    private Long resolveAverageTimeRanked(User.StatsPeriod period) {
        if (period == null || period.getCompletionTime() == null || period.getCompletions() == null) {
            return null;
        }

        Long totalTime = period.getCompletionTime().getRanked();
        Long completions = period.getCompletions().getRanked();
        if (totalTime == null || completions == null || completions == 0) {
            return null;
        }

        return totalTime / completions;
    }

    private Long findBestTime(List<Match> matches, String userUuid) {
        Long best = null;

        for (Match match : matches) {
            if (!userUuid.equals(match.getResult().getUuid())) {
                continue;
            }

            if (Boolean.TRUE.equals(match.getForfeited())) {
                continue;
            }

            Long time = match.getResult().getTime();
            if (time == null || time <= 0) {
                continue;
            }

            if (best == null || time < best) {
                best = time;
            }
        }

        return best;
    }

    private void populateAverageTimeByType(Map<Integer, Long> target, User.StatsPeriod period) {
        if (period == null || period.getCompletionTime() == null || period.getCompletions() == null) {
            return;
        }

        Long rankedTotal = period.getCompletionTime().getRanked();
        Long rankedCompletions = period.getCompletions().getRanked();
        if (rankedTotal != null && rankedCompletions != null && rankedCompletions > 0) {
            target.put(2, rankedTotal / rankedCompletions);
        }

        Long casualTotal = period.getCompletionTime().getCasual();
        Long casualCompletions = period.getCompletions().getCasual();
        if (casualTotal != null && casualCompletions != null && casualCompletions > 0) {
            target.put(1, casualTotal / casualCompletions);
        }
    }

    private Long findAverageTime(List<Match> matches, String userUuid) {
        long total = 0;
        long count = 0;

        for (Match match : matches) {
            if (!userUuid.equals(match.getResult().getUuid())) {
                continue;
            }

            if (Boolean.TRUE.equals(match.getForfeited())) {
                continue;
            }

            Long time = match.getResult().getTime();
            if (time == null || time <= 0) {
                continue;
            }

            total += time;
            count += 1;
        }

        return count == 0 ? null : total / count;
    }

    private void populateEloChange(MatchStatistics stats, List<Match> matches, String userUuid, Integer matchType) {
        if (!Integer.valueOf(2).equals(matchType)) {
            return;
        }

        int net = 0;
        int gained = 0;
        int lost = 0;
        boolean found = false;

        for (Match match : matches) {
            if (match.getChanges() == null || match.getChanges().isEmpty()) {
                continue;
            }

            for (Match.EloChange change : match.getChanges()) {
                if (!userUuid.equals(change.getUuid())) {
                    continue;
                }

                Integer delta = change.getChange();
                if (delta == null) {
                    break;
                }

                found = true;
                net += delta;
                if (delta > 0) {
                    gained += delta;
                } else if (delta < 0) {
                    lost += -delta;
                }
                break;
            }
        }

        if (!found) {
            return;
        }

        stats.setEloNet(net);
        stats.setEloGained(gained);
        stats.setEloLost(lost);
    }

    private String findBest(Map<String, StatBucket> map) {
        return map.entrySet().stream()
                .max(Comparator.comparingDouble(e -> e.getValue().getWinRate()))
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    private String findWorst(Map<String, StatBucket> map) {
        return map.entrySet().stream()
                .min(Comparator.comparingDouble(e -> e.getValue().getWinRate()))
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    private boolean isDrawResult(String resultUuid) {
        return resultUuid == null;
    }
}
