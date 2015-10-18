package org.chesscorp.club.persistence;

import org.chesscorp.club.model.ChessMove;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChessMoveRepository extends JpaRepository<ChessMove, Number> {

    List<ChessMove> findByGameId(Long gameId);
}
