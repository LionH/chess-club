package org.chesscorp.club.service;

import org.chesscorp.club.model.game.ChessGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

/**
 * Send messages through JMS queues.
 */
@Component
public class MessagingServiceImpl implements MessagingService {

    public static final String CHESS_GAME_UPDATE = "chess-game-update";

    @Autowired
    private JmsTemplate jmsTemplate;

    /**
     * Notify game update on messaging bus.
     *
     * @param game a game that has been updated
     */
    @Override
    public void notifyGameUpdated(ChessGame game) {
        jmsTemplate.send(CHESS_GAME_UPDATE, session -> {
            return session.createObjectMessage(game.getId());
        });
    }

}
