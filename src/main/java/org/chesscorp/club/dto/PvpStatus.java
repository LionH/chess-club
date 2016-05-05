package org.chesscorp.club.dto;

/**
 * Statistics of games against a defined opponent.
 */
public class PvpStatus {
    private int wins;
    private int draws;
    private int losses;

    public PvpStatus() {
        this(0, 0, 0);
    }

    public PvpStatus(int wins, int draws, int losses) {
        this.wins = wins;
        this.draws = draws;
        this.losses = losses;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getDraws() {
        return draws;
    }

    public void setDraws(int draws) {
        this.draws = draws;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    @Override
    public String toString() {
        return "PvpStatus{" +
                "wins=" + wins +
                ", draws=" + draws +
                ", losses=" + losses +
                '}';
    }
}
