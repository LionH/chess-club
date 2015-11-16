package org.chesscorp.club.model.robot;

import org.chesscorp.club.model.people.RobotPlayer;
import org.chesscorp.club.model.stats.ChessClubPosition;

import javax.persistence.*;

/**
 * Chess position reference.
 */
@Entity
@Table(name = "ROBOT_CACHE")
@SequenceGenerator(name = "ROBOT_CACHE_SEQ", initialValue = 1, allocationSize = 1, sequenceName = "ROBOT_CACHE_SEQ")
public class RobotCacheEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROBOT_CACHE_SEQ")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private RobotPlayer robotPlayer;

    @ManyToOne(fetch = FetchType.EAGER)
    private ChessClubPosition chessClubPosition;

    @Column(length = 12)
    private String pgnMoveText;

    public RobotCacheEntry() {
    }

    public RobotCacheEntry(RobotPlayer robotPlayer, ChessClubPosition chessClubPosition, String pgnMoveText) {
        this.robotPlayer = robotPlayer;
        this.chessClubPosition = chessClubPosition;
        this.pgnMoveText = pgnMoveText;
    }

    public Long getId() {
        return id;
    }

    public RobotPlayer getRobotPlayer() {
        return robotPlayer;
    }

    public ChessClubPosition getChessClubPosition() {
        return chessClubPosition;
    }

    public String getPgnMoveText() {
        return pgnMoveText;
    }

    @Override
    public String toString() {
        return "RobotCacheEntry{" +
                "id=" + id +
                ", robotPlayer=" + robotPlayer +
                ", chessClubPosition=" + chessClubPosition +
                ", pgnMoveText='" + pgnMoveText + '\'' +
                '}';
    }
}
