package org.chesscorp.club.service;

import org.chesscorp.club.ai.ChessAI;
import org.chesscorp.club.model.people.RobotPlayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Implementation of robot management.
 */
@Component
@Qualifier("direct")
public class ChessRobotServiceImpl implements ChessRobotService {
    @Autowired
    private Map<String, ChessAI> aiMap;

    @Override
    public String play(RobotPlayer robotPlayer, List<String> moves) {
        ChessAI ai = aiMap.get(robotPlayer.getEngine());

        if (ai == null) {
            throw new IllegalStateException("Unknwown AI: " + robotPlayer.getEngine() + " for robotPlayer " + robotPlayer.getId());
        }

        String robotMove = ai.computeNextMove(robotPlayer.getParameters(), moves);

        return robotMove;
    }
}
