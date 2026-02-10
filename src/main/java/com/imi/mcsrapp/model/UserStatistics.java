package com.imi.mcsrapp.model;

import java.util.HashMap;
import java.util.Map;

public class UserStatistics {

    private Long bestTimeSeason;
    private final Map<Integer, Long> bestTimeAllTimeByType = new HashMap<>();
    private final Map<Integer, Long> bestTimeSeasonByType = new HashMap<>();
    private Long averageTimeAllTimeRanked;
    private Long averageTimeSeasonRanked;
    private final Map<Integer, Long> averageTimeAllTimeByType = new HashMap<>();
    private final Map<Integer, Long> averageTimeSeasonByType = new HashMap<>();
    private final Map<Integer, MatchStatistics> matchStatisticsByType = new HashMap<>();

    public Long getBestTimeSeason() {
        return bestTimeSeason;
    }

    public void setBestTimeSeason(Long bestTimeSeason) {
        this.bestTimeSeason = bestTimeSeason;
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

    public Map<Integer, Long> getBestTimeSeasonByType() {
        return bestTimeSeasonByType;
    }

    public Map<Integer, MatchStatistics> getMatchStatisticsByType() {
        return matchStatisticsByType;
    }
}
