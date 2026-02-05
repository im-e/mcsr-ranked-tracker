package com.imi.mcsrapp.model;

public class RankTier {
    private String name;
    private String division;
    private String color;

    public RankTier() {}

    public RankTier(String name, String division, String color) {
        this.name = name;
        this.division = division;
        this.color = color;
    }

    // Static factory method to get rank tier from ELO
    public static RankTier fromElo(Integer elo) {
        if (elo == null) {
            return new RankTier("Unrated", "", "unrated");
        }

        // Netherite: 2000+
        if (elo >= 2000) return new RankTier("Netherite", "", "netherite");

        // Diamond: 1500-1999 (I: 1500+, II: 1650+, III: 1800+)
        if (elo >= 1800) return new RankTier("Diamond", "III", "diamond");
        if (elo >= 1650) return new RankTier("Diamond", "II", "diamond");
        if (elo >= 1500) return new RankTier("Diamond", "I", "diamond");

        // Emerald: 1200-1499 (I: 1200+, II: 1300+, III: 1400+)
        if (elo >= 1400) return new RankTier("Emerald", "III", "emerald");
        if (elo >= 1300) return new RankTier("Emerald", "II", "emerald");
        if (elo >= 1200) return new RankTier("Emerald", "I","emerald");

        // Gold: 900-1199 (I: 900+, II: 1000+, III: 1100+)
        if (elo >= 1100) return new RankTier("Gold", "III","gold");
        if (elo >= 1000) return new RankTier("Gold", "II","gold");
        if (elo >= 900) return new RankTier("Gold", "I", "gold");

        // Iron: 600-899 (I: 600+, II: 700+, III: 800+)
        if (elo >= 800) return new RankTier("Iron", "III","iron");
        if (elo >= 700) return new RankTier("Iron", "II",  "iron");
        if (elo >= 600) return new RankTier("Iron", "I", "iron");

        // Coal: 0-599 (I: 0+, II: 400+, III: 500+)
        if (elo >= 500) return new RankTier("Coal", "III", "coal");
        if (elo >= 400) return new RankTier("Coal", "II", "coal");
        return new RankTier("Coal", "I","coal");
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    // Get display name (e.g., "Diamond III" or "Netherite")
    public String getDisplayName() {
        if (division == null || division.isEmpty()) {
            return name;
        }
        return name + " " + division;
    }
}