package org.chesscorp.club.controllers;

import org.chesscorp.club.Application;
import org.chesscorp.club.model.people.ClubPlayer;
import org.chesscorp.club.model.people.ExternalPlayer;
import org.chesscorp.club.model.people.RobotPlayer;
import org.chesscorp.club.persistence.PlayerRepository;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.transaction.Transactional;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@Rollback
public class PlayerControllerTest {
    @Autowired
    private PlayerController playerController;

    @Autowired
    private PlayerRepository playerRepository;

    @Test
    @Transactional
    public void testSearchSuccess() throws Exception {
        playerRepository.save(new ExternalPlayer("Freddie Mercury", "freddie"));
        playerRepository.save(new ExternalPlayer("Brian May", "brian"));
        playerRepository.save(new ExternalPlayer("John Deacon", "john"));
        playerRepository.save(new ExternalPlayer("Roger Taylor", "roger"));

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(playerController).build();

        mockMvc.perform(
                post("/api/player/search").param("query", "Billy")
        ).andExpect(
                status().is2xxSuccessful()
        ).andExpect(
                jsonPath("$", hasSize(0))
        );

        mockMvc.perform(
                post("/api/player/search").param("query", "bri")
        ).andExpect(
                status().is2xxSuccessful()
        ).andExpect(
                jsonPath("$", hasSize(1))
        );

        mockMvc.perform(
                post("/api/player/search").param("query", "er")
        ).andExpect(
                status().is2xxSuccessful()
        ).andExpect(
                jsonPath("$", hasSize(2))
        );
    }

    @Test
    @Transactional
    public void testSearchAI() throws Exception {
        playerRepository.save(new ExternalPlayer("Freddie Mercury", "freddie"));
        playerRepository.save(new ClubPlayer("Brian May"));
        playerRepository.save(new RobotPlayer("GnuChess", "gnuchess", "1"));

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(playerController).build();

        mockMvc.perform(
                get("/api/player/search-ai")
        ).andExpect(
                status().is2xxSuccessful()
        ).andExpect(
                jsonPath("$", hasSize(1))
        ).andExpect(
                jsonPath("$[0].displayName", Matchers.is("GnuChess"))
        );
    }
}