package com.imi.mcsrapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.imi.mcsrapp.model.RankTier;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    private String uuid;
    private String nickname;

    @JsonProperty("roleType")
    private Integer roleType;

    @JsonProperty("eloRate")
    private Integer eloRate;

    @JsonProperty("eloRank")
    private Integer eloRank;

    private RankTier rankTier;

    private Achievements achievements;
    private Timestamp timestamp;
    private Statistics statistics;
    private Connections connections;

    @JsonProperty("weeklyRaces")
    private List<WeeklyRace> weeklyRaces;

    private String country;

    @JsonProperty("seasonResult")
    private SeasonResult seasonResult;

    // Constructors
    public User() {
    }

    // Getters and Setters
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getRoleType() {
        return roleType;
    }

    public void setRoleType(Integer roleType) {
        this.roleType = roleType;
    }

    public Integer getEloRate() {
        return eloRate;
    }

    public void setEloRate(Integer eloRate) {
        this.eloRate = eloRate;
    }

    public Integer getEloRank() {
        return eloRank;
    }

    public void setEloRank(Integer eloRank) {
        this.eloRank = eloRank;
    }

    public Achievements getAchievements() {
        return achievements;
    }

    public void setAchievements(Achievements achievements) {
        this.achievements = achievements;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

    public Connections getConnections() {
        return connections;
    }

    public void setConnections(Connections connections) {
        this.connections = connections;
    }

    public List<WeeklyRace> getWeeklyRaces() {
        return weeklyRaces;
    }

    public void setWeeklyRaces(List<WeeklyRace> weeklyRaces) {
        this.weeklyRaces = weeklyRaces;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public SeasonResult getSeasonResult() {
        return seasonResult;
    }

    public void setSeasonResult(SeasonResult seasonResult) {
        this.seasonResult = seasonResult;
    }

    @Override
    public String toString() {
        return "User{" +
                "uuid='" + uuid + '\'' +
                ", nickname='" + nickname + '\'' +
                ", eloRate=" + eloRate +
                ", eloRank=" + eloRank +
                ", country='" + country + '\'' +
                '}';
    }

    public RankTier getRankTier() {
        return rankTier;
    }

    public void setRankTier(RankTier rankTier) {
        this.rankTier = rankTier;
    }

    // Nested Classes
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Achievements {
        private List<Achievement> display;
        private List<Achievement> total;

        public List<Achievement> getDisplay() {
            return display;
        }

        public void setDisplay(List<Achievement> display) {
            this.display = display;
        }

        public List<Achievement> getTotal() {
            return total;
        }

        public void setTotal(List<Achievement> total) {
            this.total = total;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Achievement {
        private String id;
        private Long date;
        private List<String> data;
        private Integer level;
        private Object value;
        private Object goal;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Long getDate() {
            return date;
        }

        public void setDate(Long date) {
            this.date = date;
        }

        public List<String> getData() {
            return data;
        }

        public void setData(List<String> data) {
            this.data = data;
        }

        public Integer getLevel() {
            return level;
        }

        public void setLevel(Integer level) {
            this.level = level;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public Object getGoal() {
            return goal;
        }

        public void setGoal(Object goal) {
            this.goal = goal;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Timestamp {
        @JsonProperty("firstOnline")
        private Long firstOnline;

        @JsonProperty("lastOnline")
        private Long lastOnline;

        @JsonProperty("lastRanked")
        private Long lastRanked;

        @JsonProperty("nextDecay")
        private Long nextDecay;

        public Long getFirstOnline() {
            return firstOnline;
        }

        public void setFirstOnline(Long firstOnline) {
            this.firstOnline = firstOnline;
        }

        public Long getLastOnline() {
            return lastOnline;
        }

        public void setLastOnline(Long lastOnline) {
            this.lastOnline = lastOnline;
        }

        public Long getLastRanked() {
            return lastRanked;
        }

        public void setLastRanked(Long lastRanked) {
            this.lastRanked = lastRanked;
        }

        public Long getNextDecay() {
            return nextDecay;
        }

        public void setNextDecay(Long nextDecay) {
            this.nextDecay = nextDecay;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Statistics {
        private StatsPeriod season;
        private StatsPeriod total;

        public StatsPeriod getSeason() {
            return season;
        }

        public void setSeason(StatsPeriod season) {
            this.season = season;
        }

        public StatsPeriod getTotal() {
            return total;
        }

        public void setTotal(StatsPeriod total) {
            this.total = total;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class StatsPeriod {
        @JsonProperty("bestTime")
        private GameTypeStats bestTime;

        @JsonProperty("highestWinStreak")
        private GameTypeStats highestWinStreak;

        @JsonProperty("currentWinStreak")
        private GameTypeStats currentWinStreak;

        @JsonProperty("playedMatches")
        private GameTypeStats playedMatches;

        @JsonProperty("playtime")
        private GameTypeStats playtime;

        @JsonProperty("completionTime")
        private GameTypeStats completionTime;

        @JsonProperty("forfeits")
        private GameTypeStats forfeits;

        @JsonProperty("completions")
        private GameTypeStats completions;

        @JsonProperty("wins")
        private GameTypeStats wins;

        @JsonProperty("loses")
        private GameTypeStats loses;

        public GameTypeStats getBestTime() {
            return bestTime;
        }

        public void setBestTime(GameTypeStats bestTime) {
            this.bestTime = bestTime;
        }

        public GameTypeStats getHighestWinStreak() {
            return highestWinStreak;
        }

        public void setHighestWinStreak(GameTypeStats highestWinStreak) {
            this.highestWinStreak = highestWinStreak;
        }

        public GameTypeStats getCurrentWinStreak() {
            return currentWinStreak;
        }

        public void setCurrentWinStreak(GameTypeStats currentWinStreak) {
            this.currentWinStreak = currentWinStreak;
        }

        public GameTypeStats getPlayedMatches() {
            return playedMatches;
        }

        public void setPlayedMatches(GameTypeStats playedMatches) {
            this.playedMatches = playedMatches;
        }

        public GameTypeStats getPlaytime() {
            return playtime;
        }

        public void setPlaytime(GameTypeStats playtime) {
            this.playtime = playtime;
        }

        public GameTypeStats getCompletionTime() {
            return completionTime;
        }

        public void setCompletionTime(GameTypeStats completionTime) {
            this.completionTime = completionTime;
        }

        public GameTypeStats getForfeits() {
            return forfeits;
        }

        public void setForfeits(GameTypeStats forfeits) {
            this.forfeits = forfeits;
        }

        public GameTypeStats getCompletions() {
            return completions;
        }

        public void setCompletions(GameTypeStats completions) {
            this.completions = completions;
        }

        public GameTypeStats getWins() {
            return wins;
        }

        public void setWins(GameTypeStats wins) {
            this.wins = wins;
        }

        public GameTypeStats getLoses() {
            return loses;
        }

        public void setLoses(GameTypeStats loses) {
            this.loses = loses;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GameTypeStats {
        private Long ranked;
        private Long casual;

        public Long getRanked() {
            return ranked;
        }

        public void setRanked(Long ranked) {
            this.ranked = ranked;
        }

        public Long getCasual() {
            return casual;
        }

        public void setCasual(Long casual) {
            this.casual = casual;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Connections {
        private SocialConnection discord;
        private SocialConnection twitch;
        private SocialConnection youtube;

        public SocialConnection getDiscord() {
            return discord;
        }

        public void setDiscord(SocialConnection discord) {
            this.discord = discord;
        }

        public SocialConnection getTwitch() {
            return twitch;
        }

        public void setTwitch(SocialConnection twitch) {
            this.twitch = twitch;
        }

        public SocialConnection getYoutube() {
            return youtube;
        }

        public void setYoutube(SocialConnection youtube) {
            this.youtube = youtube;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SocialConnection {
        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WeeklyRace {
        private Integer id;
        private Long time;
        private Integer rank;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Long getTime() {
            return time;
        }

        public void setTime(Long time) {
            this.time = time;
        }

        public Integer getRank() {
            return rank;
        }

        public void setRank(Integer rank) {
            this.rank = rank;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SeasonResult {
        private SeasonLast last;
        private Integer highest;
        private Integer lowest;
        private List<Phase> phases;

        public SeasonLast getLast() {
            return last;
        }

        public void setLast(SeasonLast last) {
            this.last = last;
        }

        public Integer getHighest() {
            return highest;
        }

        public void setHighest(Integer highest) {
            this.highest = highest;
        }

        public Integer getLowest() {
            return lowest;
        }

        public void setLowest(Integer lowest) {
            this.lowest = lowest;
        }

        public List<Phase> getPhases() {
            return phases;
        }

        public void setPhases(List<Phase> phases) {
            this.phases = phases;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SeasonLast {
        @JsonProperty("eloRate")
        private Integer eloRate;

        @JsonProperty("eloRank")
        private Integer eloRank;

        @JsonProperty("phasePoint")
        private Integer phasePoint;

        public Integer getEloRate() {
            return eloRate;
        }

        public void setEloRate(Integer eloRate) {
            this.eloRate = eloRate;
        }

        public Integer getEloRank() {
            return eloRank;
        }

        public void setEloRank(Integer eloRank) {
            this.eloRank = eloRank;
        }

        public Integer getPhasePoint() {
            return phasePoint;
        }

        public void setPhasePoint(Integer phasePoint) {
            this.phasePoint = phasePoint;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Phase {
        private Integer phase;

        @JsonProperty("eloRate")
        private Integer eloRate;

        @JsonProperty("eloRank")
        private Integer eloRank;

        private Integer point;

        public Integer getPhase() {
            return phase;
        }

        public void setPhase(Integer phase) {
            this.phase = phase;
        }

        public Integer getEloRate() {
            return eloRate;
        }

        public void setEloRate(Integer eloRate) {
            this.eloRate = eloRate;
        }

        public Integer getEloRank() {
            return eloRank;
        }

        public void setEloRank(Integer eloRank) {
            this.eloRank = eloRank;
        }

        public Integer getPoint() {
            return point;
        }

        public void setPoint(Integer point) {
            this.point = point;
        }
    }
}


