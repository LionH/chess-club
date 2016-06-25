package org.chesscorp.club.persistence;

import org.chesscorp.club.model.stats.ChessClubPosition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.stream.Stream;

public interface ChessPositionRepository extends JpaRepository<ChessClubPosition, Number> {

    ChessClubPosition findOneByText(String position);

    Stream<ChessClubPosition> findAllByScore(Integer score);
}
