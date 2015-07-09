package org.chesscorp.club.persistence;

import org.chesscorp.club.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, String> {

}
