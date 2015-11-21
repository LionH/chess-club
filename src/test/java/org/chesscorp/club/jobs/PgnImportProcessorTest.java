package org.chesscorp.club.jobs;

import org.assertj.core.api.Assertions;
import org.chesscorp.club.Application;
import org.chesscorp.club.persistence.ChessGameRepository;
import org.chesscorp.club.persistence.ChessMoveRepository;
import org.chesscorp.club.persistence.PlayerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;

/**
 * Test the PGN import process directly. This will also implicit
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@Rollback
public class PgnImportProcessorTest {
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ChessGameRepository chessGameRepository;

    @Autowired
    private ChessMoveRepository chessMoveRepository;

    @Autowired
    private ObjectFactory<PgnImportProcessor> pgnImportProcessorObjectFactory;

    @Test
    @Transactional
    public void testImportMcDonnell() throws IOException {
        PgnImportProcessor pgnImportProcessor = pgnImportProcessorObjectFactory.getObject();
        Assertions.assertThat(playerRepository.findAll()).isEmpty();
        Assertions.assertThat(chessGameRepository.findAll()).isEmpty();

        ClassPathResource cpr = new ClassPathResource("samples-pgn/McDonnell.pgn");
        File pgnFile = cpr.getFile();
        pgnImportProcessor.process(pgnFile);

        Assertions.assertThat(playerRepository.count()).isEqualTo(8);
        Assertions.assertThat(chessGameRepository.count()).isEqualTo(106);
        Assertions.assertThat(chessMoveRepository.count()).isEqualTo(8434L);
    }

    @Test
    @Transactional
    public void testImportDeLaBourdonnais() throws IOException {
        PgnImportProcessor pgnImportProcessor = pgnImportProcessorObjectFactory.getObject();
        Assertions.assertThat(playerRepository.findAll()).isEmpty();
        Assertions.assertThat(chessGameRepository.findAll()).isEmpty();

        ClassPathResource cpr = new ClassPathResource("samples-pgn/DeLaBourdonnais.pgn");
        File pgnFile = cpr.getFile();
        pgnImportProcessor.process(pgnFile);

        Assertions.assertThat(playerRepository.count()).isEqualTo(15);
        Assertions.assertThat(chessGameRepository.count()).isEqualTo(101);
        Assertions.assertThat(chessMoveRepository.count()).isEqualTo(8204L);
    }

    @Test
    @Transactional
    public void testDualImport() throws IOException {
        PgnImportProcessor pgnImportProcessor = pgnImportProcessorObjectFactory.getObject();
        Assertions.assertThat(playerRepository.findAll()).isEmpty();
        Assertions.assertThat(chessGameRepository.findAll()).isEmpty();

        pgnImportProcessor.process(new ClassPathResource("samples-pgn/McDonnell.pgn").getFile());
        pgnImportProcessor.process(new ClassPathResource("samples-pgn/DeLaBourdonnais.pgn").getFile());

        Assertions.assertThat(playerRepository.count()).isEqualTo(19);
        Assertions.assertThat(chessGameRepository.count()).isEqualTo(122);
        Assertions.assertThat(chessMoveRepository.count()).isEqualTo(9381L);
    }
}
