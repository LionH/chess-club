package org.chesscorp.club.persistence;

import org.chesscorp.club.model.EloRank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EloRankRepository extends JpaRepository<EloRank, Number> {

    List<EloRank> findByPlayerId(Long gameId);
}
