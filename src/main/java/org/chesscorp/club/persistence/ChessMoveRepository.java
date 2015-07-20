package org.chesscorp.club.persistence;

import org.chesscorp.club.model.ChessMove;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChessMoveRepository extends JpaRepository<ChessMove, String> {

}
