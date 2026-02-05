package com.imi.mcsrapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Match {
    
    private Long id;
    private Integer type;
    private Seed seed;
    private String category;
    
    @JsonProperty("gameMode")
    private String gameMode;
    
    private List<Player> players;
    private List<Player> spectators;
    private MatchResult result;
    private Boolean forfeited;
    private Boolean decayed;
    private Rank rank;
    private List<Vod> vod;
    private List<EloChange> changes;
    private Boolean beginner;
    
    @JsonProperty("botSource")
    private String botSource;
    
    private Integer season;
    private Long date;
    
    @JsonProperty("seedType")
    private String seedType;
    
    @JsonProperty("bastionType")
    private String bastionType;
    
    private String tag;

    // Constructors
    public Match() {
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Seed getSeed() {
        return seed;
    }

    public void setSeed(Seed seed) {
        this.seed = seed;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<Player> getSpectators() {
        return spectators;
    }

    public void setSpectators(List<Player> spectators) {
        this.spectators = spectators;
    }

    public MatchResult getResult() {
        return result;
    }

    public void setResult(MatchResult result) {
        this.result = result;
    }

    public Boolean getForfeited() {
        return forfeited;
    }

    public void setForfeited(Boolean forfeited) {
        this.forfeited = forfeited;
    }

    public Boolean getDecayed() {
        return decayed;
    }

    public void setDecayed(Boolean decayed) {
        this.decayed = decayed;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public List<Vod> getVod() {
        return vod;
    }

    public void setVod(List<Vod> vod) {
        this.vod = vod;
    }

    public List<EloChange> getChanges() {
        return changes;
    }

    public void setChanges(List<EloChange> changes) {
        this.changes = changes;
    }

    public Boolean getBeginner() {
        return beginner;
    }

    public void setBeginner(Boolean beginner) {
        this.beginner = beginner;
    }

    public String getBotSource() {
        return botSource;
    }

    public void setBotSource(String botSource) {
        this.botSource = botSource;
    }

    public Integer getSeason() {
        return season;
    }

    public void setSeason(Integer season) {
        this.season = season;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getSeedType() {
        return seedType;
    }

    public void setSeedType(String seedType) {
        this.seedType = seedType;
    }

    public String getBastionType() {
        return bastionType;
    }

    public void setBastionType(String bastionType) {
        this.bastionType = bastionType;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    // Nested Classes
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Seed {
        private String id;
        private String overworld;
        private String nether;
        
        @JsonProperty("endTowers")
        private List<Integer> endTowers;
        
        private List<String> variations;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOverworld() {
            return overworld;
        }

        public void setOverworld(String overworld) {
            this.overworld = overworld;
        }

        public String getNether() {
            return nether;
        }

        public void setNether(String nether) {
            this.nether = nether;
        }

        public List<Integer> getEndTowers() {
            return endTowers;
        }

        public void setEndTowers(List<Integer> endTowers) {
            this.endTowers = endTowers;
        }

        public List<String> getVariations() {
            return variations;
        }

        public void setVariations(List<String> variations) {
            this.variations = variations;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Player {
        private String uuid;
        private String nickname;
        
        @JsonProperty("roleType")
        private Integer roleType;
        
        @JsonProperty("eloRate")
        private Integer eloRate;
        
        @JsonProperty("eloRank")
        private Integer eloRank;
        
        private String country;

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

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MatchResult {
        private String uuid;
        private Long time;

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public Long getTime() {
            return time;
        }

        public void setTime(Long time) {
            this.time = time;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Rank {
        private Integer season;
        private Integer allTime;

        public Integer getSeason() {
            return season;
        }

        public void setSeason(Integer season) {
            this.season = season;
        }

        public Integer getAllTime() {
            return allTime;
        }

        public void setAllTime(Integer allTime) {
            this.allTime = allTime;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Vod {
        private String uuid;
        private String url;
        
        @JsonProperty("startsAt")
        private Long startsAt;

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Long getStartsAt() {
            return startsAt;
        }

        public void setStartsAt(Long startsAt) {
            this.startsAt = startsAt;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class EloChange {
        private String uuid;
        private Integer change;
        
        @JsonProperty("eloRate")
        private Integer eloRate;

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public Integer getChange() {
            return change;
        }

        public void setChange(Integer change) {
            this.change = change;
        }

        public Integer getEloRate() {
            return eloRate;
        }

        public void setEloRate(Integer eloRate) {
            this.eloRate = eloRate;
        }
    }
}
