package org.chesscorp.club.persistence;

import org.chesscorp.club.model.people.ClubPlayer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.stream.Stream;

public interface ClubPlayerRepository extends JpaRepository<ClubPlayer, Number> {

    Stream<ClubPlayer> findAllByActive(boolean active);
}
