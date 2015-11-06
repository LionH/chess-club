package org.chesscorp.club.service;

import org.chesscorp.club.model.people.RobotPlayer;

import java.util.List;

/**
 * Robot player AIs management.
 */
public interface ChessRobotService {

    /**
     * Return next move played by the selected robot.
     *
     * @param robotPlayer a robot player
     * @param moves       a list of game moves in PGN movetext notation
     * @return the robot move in pgn movetext, or null if no move was available.
     */
    String play(RobotPlayer robotPlayer, List<String> moves);
}
