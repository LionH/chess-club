package org.chesscorp.club.persistence;

import org.chesscorp.club.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, String> {

    List<Player> findByDisplayName(String displayName);
}
