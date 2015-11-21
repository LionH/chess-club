package org.chesscorp.club.integration;

import org.assertj.core.api.Assertions;
import org.chesscorp.club.Application;
import org.chesscorp.club.controllers.ChessGameController;
import org.chesscorp.club.dto.PlayerProfile;
import org.chesscorp.club.jobs.PgnImportProcessor;
import org.chesscorp.club.model.people.Player;
import org.chesscorp.club.persistence.ChessGameRepository;
import org.chesscorp.club.persistence.ChessMoveRepository;
import org.chesscorp.club.persistence.PlayerRepository;
import org.chesscorp.club.service.ChessGameService;
import org.chesscorp.club.service.PlayerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.transaction.Transactional;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test the PGN import process directly. This will also implicit
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@Rollback
public class ProfileAfterImportTest {
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private ChessGameRepository chessGameRepository;
    @Autowired
    private ChessMoveRepository chessMoveRepository;
    @Autowired
    private ObjectFactory<PgnImportProcessor> pgnImportProcessorObjectFactory;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private ChessGameController chessGameController;
    @Autowired
    private ChessGameService chessGameService;

    @Test
    @Transactional
    public void testImportedProfile() throws Exception {
        PgnImportProcessor pgnImportProcessor = pgnImportProcessorObjectFactory.getObject();
        Assertions.assertThat(playerRepository.findAll()).isEmpty();
        Assertions.assertThat(chessGameRepository.findAll()).isEmpty();

        pgnImportProcessor.process(new ClassPathResource("samples-pgn/McDonnell.pgn").getFile());
        pgnImportProcessor.process(new ClassPathResource("samples-pgn/DeLaBourdonnais.pgn").getFile());

        Assertions.assertThat(playerRepository.count()).isEqualTo(19);
        Assertions.assertThat(chessGameRepository.count()).isEqualTo(122);
        Assertions.assertThat(chessMoveRepository.count()).isEqualTo(9381L);

        Player player = playerRepository.findByDisplayName("De Labourdonnais, Louis").get(0);
        PlayerProfile profile = playerService.getProfile(player.getId());

        /*
         * Search using the Game service.
         */

        Assertions.assertThat(profile).isNotNull();
        Assertions.assertThat(chessGameService.searchGames(player.getId(), true)).hasSize(0);
        Assertions.assertThat(chessGameService.searchGames(player.getId(), false)).hasSize(86);
        Assertions.assertThat(chessGameService.searchGames(player.getId(), null)).hasSize(86);

        /*
         * Search using the controller to validate JSON serialization too.
         */

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(chessGameController).build();

        mockMvc.perform(
                post("/api/chess/game/search").param("playerId", player.getId().toString())
        ).andExpect(
                status().is2xxSuccessful()
        ).andExpect(
                jsonPath("$", hasSize(86))
        );
    }
}
