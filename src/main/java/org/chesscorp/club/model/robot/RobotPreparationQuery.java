package org.chesscorp.club.model.robot;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Query robot cache preparation.
 */
public class RobotPreparationQuery implements Serializable {

    private Long robotId;

    private List<String> moves;

    private int halfMoves;

    public RobotPreparationQuery(Long robotId, List<String> moves, int halfMoves) {
        this.robotId = robotId;
        this.moves = moves;
        this.halfMoves = halfMoves;
    }

    public RobotPreparationQuery() {
    }

    public Long getRobotId() {
        return robotId;
    }

    public List<String> getMoves() {
        return Collections.unmodifiableList(moves);
    }

    public int getHalfMoves() {
        return halfMoves;
    }

    @Override
    public String toString() {
        return "RobotPreparationQuery{" +
                "robotId=" + robotId +
                ", moves=" + moves +
                ", halfMoves=" + halfMoves +
                '}';
    }
}
