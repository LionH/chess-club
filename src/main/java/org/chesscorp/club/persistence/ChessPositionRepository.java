package org.chesscorp.club.persistence;

import org.chesscorp.club.model.stats.ChessPosition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChessPositionRepository extends JpaRepository<ChessPosition, String> {

    ChessPosition findOneByText(Long gameId);
}
