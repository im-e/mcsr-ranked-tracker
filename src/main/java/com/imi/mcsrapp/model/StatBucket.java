package com.imi.mcsrapp.model;

public class StatBucket {

    private int matches;
    private int wins;
    private int losses;

    private int finishedWins;
    private long totalWinTime;
    private Long fastestTime;

    private int forfeitWins;
    private int forfeitLosses;

    public void recordMatch(boolean isWin, boolean isForfeit, Long time) {
        matches++;

        if (isWin) {
            wins++;

            if (isForfeit) {
                forfeitWins++;
            } else if (time != null && time > 0) {
                finishedWins++;
                totalWinTime += time;

                if (fastestTime == null || time < fastestTime) {
                    fastestTime = time;
                }
            }
        } else {
            losses++;
            if (isForfeit) {
                forfeitLosses++;
            }
        }
    }

    public double getWinRate() {
        return matches == 0 ? 0.0 : (wins * 100.0) / matches;
    }

    public double getForfeitRate() {
        return matches == 0 ? 0.0 : (forfeitLosses * 100.0) / matches;
    }

    public Long getAverageWinTime() {
        return finishedWins == 0 ? null : totalWinTime / finishedWins;
    }

    public int getTotalForfeits() {
        return forfeitWins + forfeitLosses;
    }


    public int getMatches() {
        return matches;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public int getFinishedWins() {
        return finishedWins;
    }

    public Long getFastestTime() {
        return fastestTime;
    }

    public int getForfeitWins() {
        return forfeitWins;
    }

    public int getForfeitLosses() {
        return forfeitLosses;
    }
}
