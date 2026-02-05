package com.imi.mcsrapp.model;

import java.util.HashMap;
import java.util.Map;

public class UserStatistics {

    private int totalMatches;
    private int validMatches;

    private final StatBucket overall = new StatBucket();
    private final Map<String, StatBucket> bastionStats = new HashMap<>();
    private final Map<String, StatBucket> seedStats = new HashMap<>();

    private Long bestTimeSeason;
    private Long bestTimeLastMatches;
    private final Map<Integer, Long> bestTimeAllTimeByType = new HashMap<>();
    private final Map<Integer, Long> bestTimeSeasonByType = new HashMap<>();
    private final Map<Integer, Long> bestTimeLastMatchesByType = new HashMap<>();
    private Long averageTimeAllTimeRanked;
    private Long averageTimeSeasonRanked;
    private final Map<Integer, Long> averageTimeAllTimeByType = new HashMap<>();
    private final Map<Integer, Long> averageTimeSeasonByType = new HashMap<>();
    private final Map<Integer, Long> averageTimeLastMatchesByType = new HashMap<>();

    private String bestBastion;
    private String worstBastion;
    private String bestSeed;
    private String worstSeed;

    public int getTotalMatches() {
        return totalMatches;
    }

    public void setTotalMatches(int totalMatches) {
        this.totalMatches = totalMatches;
    }

    public int getValidMatches() {
        return validMatches;
    }

    public void setValidMatches(int validMatches) {
        this.validMatches = validMatches;
    }

    public StatBucket getOverall() {
        return overall;
    }

    public Map<String, StatBucket> getBastionStats() {
        return bastionStats;
    }

    public Map<String, StatBucket> getSeedStats() {
        return seedStats;
    }

    public Long getBestTimeSeason() {
        return bestTimeSeason;
    }

    public void setBestTimeSeason(Long bestTimeSeason) {
        this.bestTimeSeason = bestTimeSeason;
    }

    public Long getBestTimeLastMatches() {
        return bestTimeLastMatches;
    }

    public void setBestTimeLastMatches(Long bestTimeLastMatches) {
        this.bestTimeLastMatches = bestTimeLastMatches;
    }

    public Map<Integer, Long> getBestTimeAllTimeByType() {
        return bestTimeAllTimeByType;
    }

    public Long getAverageTimeAllTimeRanked() {
        return averageTimeAllTimeRanked;
    }

    public void setAverageTimeAllTimeRanked(Long averageTimeAllTimeRanked) {
        this.averageTimeAllTimeRanked = averageTimeAllTimeRanked;
    }

    public Long getAverageTimeSeasonRanked() {
        return averageTimeSeasonRanked;
    }

    public void setAverageTimeSeasonRanked(Long averageTimeSeasonRanked) {
        this.averageTimeSeasonRanked = averageTimeSeasonRanked;
    }

    public Map<Integer, Long> getAverageTimeAllTimeByType() {
        return averageTimeAllTimeByType;
    }

    public Map<Integer, Long> getAverageTimeSeasonByType() {
        return averageTimeSeasonByType;
    }

    public Map<Integer, Long> getAverageTimeLastMatchesByType() {
        return averageTimeLastMatchesByType;
    }

    public Map<Integer, Long> getBestTimeSeasonByType() {
        return bestTimeSeasonByType;
    }

    public Map<Integer, Long> getBestTimeLastMatchesByType() {
        return bestTimeLastMatchesByType;
    }

    public String getBestBastion() {
        return bestBastion;
    }

    public void setBestBastion(String bestBastion) {
        this.bestBastion = bestBastion;
    }

    public String getWorstBastion() {
        return worstBastion;
    }

    public void setWorstBastion(String worstBastion) {
        this.worstBastion = worstBastion;
    }

    public String getBestSeed() {
        return bestSeed;
    }

    public void setBestSeed(String bestSeed) {
        this.bestSeed = bestSeed;
    }

    public String getWorstSeed() {
        return worstSeed;
    }

    public void setWorstSeed(String worstSeed) {
        this.worstSeed = worstSeed;
    }
}
