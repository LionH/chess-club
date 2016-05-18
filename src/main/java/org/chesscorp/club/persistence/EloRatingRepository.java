package org.chesscorp.club.persistence;

import org.chesscorp.club.model.game.EloRating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EloRatingRepository extends JpaRepository<EloRating, Number> {

    EloRating findFirstByPlayerIdOrderByIdDesc(Long playerId);

    List<EloRating> findByPlayerIdOrderByScoreDateAsc(Long playerId);
}
