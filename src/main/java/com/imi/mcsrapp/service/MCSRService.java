package com.imi.mcsrapp.service;

import com.imi.mcsrapp.model.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.HashMap;
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
        APIResponse<List<Match>> response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/users/{identifier}/matches")
                        .queryParam("count", count != null ? count : 20)
                        .build(identifier))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<APIResponse<List<Match>>>() {})
                .block();

        return response != null ? response.getData() : null;
    }

    public UserStatistics getUserMatchStatistics(String identifier, Integer count, Integer matchType) {
        List<Match> matches = getUserMatchesSync(identifier, count);
        if (matches == null || matches.isEmpty()) {
            return null;
        }

        User user = getUserByIdentifierSync(identifier);
        return calculateMatchStatistics(matches, user, identifier, matchType);
    }

    private UserStatistics calculateMatchStatistics(List<Match> matches, User user, String identifier, Integer matchType) {
        UserStatistics stats = new UserStatistics();

        String userUuid = matches.stream()
                .flatMap(m -> m.getPlayers().stream())
                .filter(p -> p.getNickname().equalsIgnoreCase(identifier)
                        || p.getUuid().equals(identifier))
                .findFirst()
                .map(Match.Player::getUuid)
                .orElse(null);

        if (userUuid == null) {
            return stats;
        }

        populateBestTimesFromUserStats(stats, user, matchType);

        List<Match> validMatchesAllTypes = matches.stream()
                .filter(m -> !Boolean.TRUE.equals(m.getDecayed()) && m.getResult() != null)
                .toList();

        List<Match> validMatches = matches.stream()
                .filter(m -> matchType == null || matchType.equals(m.getType()))
                .filter(m -> !Boolean.TRUE.equals(m.getDecayed()) && m.getResult() != null)
                .toList();

        stats.setTotalMatches((int) matches.stream()
                .filter(m -> matchType == null || matchType.equals(m.getType()))
                .count());

        stats.setValidMatches(validMatches.size());
        stats.setBestTimeLastMatches(findBestTime(validMatches, userUuid));

        Map<Integer, long[]> lastMatchAverages = new HashMap<>();
        for (Match match : validMatchesAllTypes) {
            updateBestTimeByType(stats.getBestTimeLastMatchesByType(), match, userUuid);
            updateAverageTotalsByType(lastMatchAverages, match, userUuid);
        }
        finalizeAverageTimes(stats.getAverageTimeLastMatchesByType(), lastMatchAverages);

        for (Match match : validMatches) {
            boolean isWin = userUuid.equals(match.getResult().getUuid());
            boolean isForfeit = Boolean.TRUE.equals(match.getForfeited());
            Long time = isWin ? match.getResult().getTime() : null;

            // Overall
            stats.getOverall().recordMatch(isWin, isForfeit, time);

            // Bastion
            String bastion = match.getBastionType() != null ? match.getBastionType() : "UNKNOWN";
            stats.getBastionStats()
                    .computeIfAbsent(bastion, k -> new StatBucket())
                    .recordMatch(isWin, isForfeit, time);

            // Seed
            String seed = match.getSeedType() != null ? match.getSeedType() : "UNKNOWN";
            stats.getSeedStats()
                    .computeIfAbsent(seed, k -> new StatBucket())
                    .recordMatch(isWin, isForfeit, time);
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

    private void populateBestTimesFromUserStats(UserStatistics stats, User user, Integer matchType) {
        if (user == null || user.getStatistics() == null) {
            return;
        }

        User.StatsPeriod season = user.getStatistics().getSeason();
        User.StatsPeriod total = user.getStatistics().getTotal();

        stats.setBestTimeSeason(resolveBestTime(season, matchType));
        stats.setAverageTimeSeasonRanked(resolveAverageTimeRanked(season));
        stats.setAverageTimeAllTimeRanked(resolveAverageTimeRanked(total));

        populateBestTimeByType(stats.getBestTimeAllTimeByType(), total);
        populateBestTimeByType(stats.getBestTimeSeasonByType(), season);
        populateAverageTimeByType(stats.getAverageTimeAllTimeByType(), total);
        populateAverageTimeByType(stats.getAverageTimeSeasonByType(), season);
    }

    private Long resolveBestTime(User.StatsPeriod period, Integer matchType) {
        if (period == null || period.getBestTime() == null) {
            return null;
        }

        Long ranked = period.getBestTime().getRanked();
        Long casual = period.getBestTime().getCasual();

        if (matchType == null) {
            return minNonNull(ranked, casual);
        }

        if (matchType == 1) {
            return casual;
        }

        if (matchType == 2) {
            return ranked;
        }

        return null;
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

    private Long minNonNull(Long first, Long second) {
        if (first == null) {
            return second;
        }

        if (second == null) {
            return first;
        }

        return Math.min(first, second);
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

    private void updateBestTimeByType(Map<Integer, Long> target, Match match, String userUuid) {
        if (match.getType() == null) {
            return;
        }

        if (!userUuid.equals(match.getResult().getUuid())) {
            return;
        }

        if (Boolean.TRUE.equals(match.getForfeited())) {
            return;
        }

        Long time = match.getResult().getTime();
        if (time == null || time <= 0) {
            return;
        }

        Long current = target.get(match.getType());
        if (current == null || time < current) {
            target.put(match.getType(), time);
        }
    }

    private void updateAverageTotalsByType(Map<Integer, long[]> target, Match match, String userUuid) {
        Integer type = match.getType();
        if (type == null) {
            return;
        }

        if (!userUuid.equals(match.getResult().getUuid())) {
            return;
        }

        if (Boolean.TRUE.equals(match.getForfeited())) {
            return;
        }

        Long time = match.getResult().getTime();
        if (time == null || time <= 0) {
            return;
        }

        long[] totals = target.computeIfAbsent(type, k -> new long[2]);
        totals[0] += time;
        totals[1] += 1;
    }

    private void finalizeAverageTimes(Map<Integer, Long> target, Map<Integer, long[]> totals) {
        for (Map.Entry<Integer, long[]> entry : totals.entrySet()) {
            long count = entry.getValue()[1];
            if (count > 0) {
                target.put(entry.getKey(), entry.getValue()[0] / count);
            }
        }
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
}
