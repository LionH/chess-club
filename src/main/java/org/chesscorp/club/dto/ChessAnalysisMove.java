package org.chesscorp.club.dto;

/**
 * Single move analysis model
 */
public class ChessAnalysisMove {

    private Long moveId;

    private int index;

    private int score;

    private String expected;

    public ChessAnalysisMove(Long moveId, int index, int score, String expected) {
        this.moveId = moveId;
        this.index = index;
        this.score = score;
        this.expected = expected;
    }

    public Long getMoveId() {
        return moveId;
    }

    public int getIndex() {
        return index;
    }

    public int getScore() {
        return score;
    }

    public String getExpected() {
        return expected;
    }

    @Override
    public String toString() {
        return "ChessAnalysisMove{" +
                "moveId=" + moveId +
                ", index=" + index +
                ", score=" + score +
                ", expected='" + expected + '\'' +
                '}';
    }
}
