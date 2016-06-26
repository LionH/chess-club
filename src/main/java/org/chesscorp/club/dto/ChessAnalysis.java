package org.chesscorp.club.dto;

import java.util.List;

/**
 * Game analysis model.
 */
public class ChessAnalysis {

    private Long gameId;

    private List<ChessAnalysisMove> movesAnalysis;

    private ChessAnalysis() {
    }

    public ChessAnalysis(Long gameId, List<ChessAnalysisMove> movesAnalysis) {
        this.gameId = gameId;
        this.movesAnalysis = movesAnalysis;
    }

    public Long getGameId() {
        return gameId;
    }

    public List<ChessAnalysisMove> getMovesAnalysis() {
        return movesAnalysis;
    }

    @Override
    public String toString() {
        return "ChessAnalysis{" +
                "gameId=" + gameId +
                ", movesAnalysis=" + movesAnalysis +
                '}';
    }
}
