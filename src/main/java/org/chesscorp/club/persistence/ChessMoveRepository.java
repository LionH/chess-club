package org.chesscorp.club.persistence;

import org.chesscorp.club.model.game.ChessMove;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.stream.Stream;

public interface ChessMoveRepository extends JpaRepository<ChessMove, Number> {

    List<ChessMove> findByGameId(Long gameId);

    /**
     * Find all moves over a certain ID. Mostly used for batch iteration.
     *
     * @param moveId a move ID identifier
     * @return all the matching moves
     */
    Stream<ChessMove> findAllByIdGreaterThan(Long moveId);

    void deleteByIdGreaterThan(Long moveId);

    /**
     * Find moves over a certain ID. Mostly used for batch iteration.
     *
     * @param moveId a move ID identifier
     * @return all the matching moves
     */
    Stream<ChessMove> findFirst1000ByIdGreaterThan(Long moveId);
}
