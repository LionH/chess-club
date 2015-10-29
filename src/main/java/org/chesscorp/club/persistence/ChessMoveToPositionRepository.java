package org.chesscorp.club.persistence;

import org.chesscorp.club.model.stats.ChessMoveToPosition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChessMoveToPositionRepository extends JpaRepository<ChessMoveToPosition, Long> {

    ChessMoveToPosition findFirstByOrderByChessMoveIdDesc();
}
