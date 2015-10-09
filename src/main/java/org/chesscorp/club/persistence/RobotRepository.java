package org.chesscorp.club.persistence;

import org.chesscorp.club.model.Robot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RobotRepository extends JpaRepository<Robot, Number> {

}
