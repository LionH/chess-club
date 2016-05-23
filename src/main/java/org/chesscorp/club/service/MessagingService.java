package org.chesscorp.club.service;

import org.chesscorp.club.model.game.ChessGame;

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
}
