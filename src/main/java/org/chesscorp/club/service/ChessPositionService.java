package org.chesscorp.club.service;

import org.chesscorp.club.model.game.ChessGame;
import org.chesscorp.club.model.stats.ChessClubPosition;

import java.util.List;

/**
 * Access/update positions in chess games.
 */
public interface ChessPositionService {

    /**
     * Compute positions for new moves in the repository.
     *
     * @return number of moves updated
     */
    long updateMovePositions();

    /**
     * Compute positions for a given game.
     *
     * @param gameId identifier of a newly imported game.
     * @return number of moves updated
     */
    long updateGamePositions(Number gameId);

    /**
     * Find games that are related based on the current position.
     *
     * @param gameId identifier of a game
     * @return a list of game which had the same position
     */
    List<ChessGame> findRelatedGames(Number gameId);

    /**
     * Find a position in the repository or create it implicitly.
     *
     * @param positionText the position serialized as raw string
     * @return a position object from the repository
     */
    ChessClubPosition findOrCreatePosition(String positionText);
}
