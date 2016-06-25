package org.chesscorp.club.service;

/**
 * Position analysis interface.
 */
public interface ChessAnalysisService {

    /**
     * Analze a position, by evaluating score and other items
     *
     * @param positionId the identifier of the position to be analyzed
     */
    void analyzePosition(long positionId);
}
