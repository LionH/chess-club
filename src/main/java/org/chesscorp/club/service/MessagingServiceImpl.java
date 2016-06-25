package org.chesscorp.club.service;

import org.chesscorp.club.model.game.ChessGame;
import org.chesscorp.club.model.people.RobotPlayer;
import org.chesscorp.club.model.robot.RobotPreparationQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Send messages through JMS queues.
 */
@Component
public class MessagingServiceImpl implements MessagingService {

    @Autowired
    private JmsTemplate jmsTemplate;

    /**
     * Notify game update on messaging bus.
     *
     * @param game a game that has been updated
     */
    @Override
    public void notifyGameUpdated(ChessGame game) {
        jmsTemplate.send("chess-game-updated", session -> {
            return session.createObjectMessage(game.getId());
        });
    }

    @Override
    public void notifyPrepareRobot(RobotPlayer robotPlayer, List<String> moves, int halfMoves) {
        jmsTemplate.send("robot-prepare", session -> {
            return session.createObjectMessage(new RobotPreparationQuery(
                    robotPlayer.getId(), moves, halfMoves
            ));
        });
    }

    @Override
    public void notifyPositionCreated(Long id) {
        jmsTemplate.send("chess-position-created", session -> {
            return session.createObjectMessage(id);
        });
    }

}
