package com.imi.mcsrapp.model;

import java.util.HashMap;
import java.util.Map;

public class MatchStatistics {

    private int totalMatches;
    private int validMatches;
    private int decayedMatches;
    private int missingResultMatches;

    private final StatBucket overall = new StatBucket();
    private final Map<String, StatBucket> bastionStats = new HashMap<>();
    private final Map<String, StatBucket> seedStats = new HashMap<>();

    private Long bestTimeLastMatches;
    private Long averageTimeLastMatches;

    private Integer eloNet;
    private Integer eloGained;
    private Integer eloLost;

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

    public int getDecayedMatches() {
        return decayedMatches;
    }

    public void setDecayedMatches(int decayedMatches) {
        this.decayedMatches = decayedMatches;
    }

    public int getMissingResultMatches() {
        return missingResultMatches;
    }

    public void setMissingResultMatches(int missingResultMatches) {
        this.missingResultMatches = missingResultMatches;
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

    public Long getBestTimeLastMatches() {
        return bestTimeLastMatches;
    }

    public void setBestTimeLastMatches(Long bestTimeLastMatches) {
        this.bestTimeLastMatches = bestTimeLastMatches;
    }

    public Long getAverageTimeLastMatches() {
        return averageTimeLastMatches;
    }

    public void setAverageTimeLastMatches(Long averageTimeLastMatches) {
        this.averageTimeLastMatches = averageTimeLastMatches;
    }

    public Integer getEloNet() {
        return eloNet;
    }

    public void setEloNet(Integer eloNet) {
        this.eloNet = eloNet;
    }

    public Integer getEloGained() {
        return eloGained;
    }

    public void setEloGained(Integer eloGained) {
        this.eloGained = eloGained;
    }

    public Integer getEloLost() {
        return eloLost;
    }

    public void setEloLost(Integer eloLost) {
        this.eloLost = eloLost;
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
