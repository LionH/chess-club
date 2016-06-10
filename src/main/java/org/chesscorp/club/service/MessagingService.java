package org.chesscorp.club.service;

import org.chesscorp.club.model.game.ChessGame;
import org.chesscorp.club.model.people.RobotPlayer;

import java.util.List;

/**
 * Send messages to external services.
 */
public interface MessagingService {

    /**
     * Notify for a game change (creation or new move).
     *
     * @param game the game that has been updated.
     */
    void notifyGameUpdated(ChessGame game);

    /**
     * Prepare robot to next half moves.
     *
     * @param robotPlayer the robot player identifier.
     * @param moves       the current game moves (ie. position).
     * @param halfMoves   the preparation depth.
     */
    void notifyPrepareRobot(RobotPlayer robotPlayer, List<String> moves, int halfMoves);
}
