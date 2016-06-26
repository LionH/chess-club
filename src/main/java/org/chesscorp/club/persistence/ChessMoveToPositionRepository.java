package org.chesscorp.club.persistence;

import org.chesscorp.club.model.stats.ChessMoveToPosition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChessMoveToPositionRepository extends JpaRepository<ChessMoveToPosition, Number> {

    /**
     * Find the most recent indexed move.
     *
     * @return the most recently indexed move, or null if no move was indexed yet.
     */
    ChessMoveToPosition findFirstByOrderByChessMoveIdDesc();

    /**
     * Find 10 distinct moves for a given position ID.
     *
     * @param positionId an existing position ID
     * @return a collection of distinct moves
     */
    List<ChessMoveToPosition> findFirst10ByChessClubPositionId(Long positionId);
}
