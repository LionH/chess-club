package org.chesscorp.club.persistence;

import org.chesscorp.club.model.people.ExternalPlayer;
import org.chesscorp.club.model.people.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.stream.Stream;

public interface PlayerRepository extends JpaRepository<Player, Number> {

    List<Player> findByDisplayName(String displayName);

    ExternalPlayer findOneByNormalizedName(String normalizedName);

    @Query(value = "SELECT p from Player p")
    Stream<Player> streamAllPlayers();
}
