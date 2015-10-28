package org.chesscorp.club.persistence;

import org.chesscorp.club.model.people.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, String> {

}
