package org.chesscorp.club.persistence;

import org.chesscorp.club.model.people.ExternalPlayer;
import org.chesscorp.club.model.people.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Number> {

    List<Player> findByDisplayName(String displayName);

    List<ExternalPlayer> findByNormalizedName(String displayName);
}
