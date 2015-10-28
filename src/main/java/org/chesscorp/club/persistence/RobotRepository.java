package org.chesscorp.club.persistence;

import org.chesscorp.club.model.people.RobotPlayer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RobotRepository extends JpaRepository<RobotPlayer, Number> {

}
