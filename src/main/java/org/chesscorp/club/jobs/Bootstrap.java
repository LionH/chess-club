package org.chesscorp.club.jobs;


import org.chesscorp.club.model.Player;
import org.chesscorp.club.persistence.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class Bootstrap {

    @Autowired
    private PlayerRepository playerRepository;

    @PostConstruct
    public void populate() {
        if (playerRepository.count() == 0) {
            playerRepository.save(new Player("alcibiade", "Alcibiade"));
            playerRepository.save(new Player("john", "John"));
            playerRepository.save(new Player("bob", "Bob"));
            playerRepository.save(new Player("steve", "Steve"));
        }
    }
}
