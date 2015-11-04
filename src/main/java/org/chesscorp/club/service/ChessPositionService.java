package org.chesscorp.club.service;

import org.chesscorp.club.model.game.ChessGame;

import java.util.List;

/**
 * Access/update positions in chess games.
 */
public interface ChessPositionService {

    /**
     * Compute positions for new moves in the repository.
     */
    void updateMovePositions();

    /**
     * Find games that are related based on the current position.
     *
     * @param gameId identifier of a game
     * @return a list of game which had the same position
     */
    List<ChessGame> findRelatedGames(Number gameId);
}
