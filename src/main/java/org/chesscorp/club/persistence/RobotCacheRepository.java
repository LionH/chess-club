package org.chesscorp.club.persistence;

import org.chesscorp.club.model.robot.RobotCacheEntry;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Persistence of robots moves.
 */
public interface RobotCacheRepository extends JpaRepository<RobotCacheEntry, Long> {

}
