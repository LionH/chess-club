package org.chesscorp.club.persistence;

import org.chesscorp.club.model.EloRating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EloRatingRepository extends JpaRepository<EloRating, Number> {

    List<EloRating> findByPlayerId(Long playerId);

    EloRating findFirstByPlayerIdOrderByIdDesc(Long playerId);
}
