package org.chesscorp.club.persistence;

import org.chesscorp.club.model.ChessGame;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChessGameRepository extends JpaRepository<ChessGame, String> {

}
