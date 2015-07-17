package org.chesscorp.club.persistence;

import org.chesscorp.club.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {

}
