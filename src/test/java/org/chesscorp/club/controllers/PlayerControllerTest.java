package org.chesscorp.club.controllers;

import org.assertj.core.api.Assertions;
import org.chesscorp.club.Application;
import org.chesscorp.club.model.people.Player;
import org.chesscorp.club.persistence.PlayerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import javax.transaction.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@TransactionConfiguration(defaultRollback = true)
public class PlayerControllerTest {
    @Autowired
    private PlayerController playerController;

    @Autowired
    private PlayerRepository playerRepository;

    @Test
    @Transactional
    public void testSearchSuccess() {
        playerRepository.save(new Player("Freddie Mercury"));
        playerRepository.save(new Player("Brian May"));
        playerRepository.save(new Player("John Deacon"));
        playerRepository.save(new Player("Roger Taylor"));

        Assertions.assertThat(playerController.search("Billy")).isEmpty();
        Assertions.assertThat(playerController.search("bri")).hasSize(1);
        Assertions.assertThat(playerController.search("er")).hasSize(2);
    }
}