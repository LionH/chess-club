package org.chesscorp.club.jobs;

import org.alcibiade.chess.persistence.PgnBookReader;
import org.alcibiade.chess.persistence.PgnGameModel;
import org.chesscorp.club.model.game.ChessGame;
import org.chesscorp.club.monitoring.PerformanceMonitor;
import org.chesscorp.club.service.ChessGameService;
import org.chesscorp.club.service.ChessPositionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Import a single PGN file in the database.
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PgnImportProcessor {

    private Logger logger = LoggerFactory.getLogger(PgnImportProcessor.class);

    @Autowired
    private ChessGameService chessGameService;

    @Autowired
    private ChessPositionService chessPositionService;

    @Autowired
    private PerformanceMonitor performanceMonitor;

    @ServiceActivator
    public File process(File file) {
        logger.info("Importing games from " + file);

        try (InputStream pgnStream = new FileInputStream(file)) {
            performanceMonitor.mark();

            long importCount = 0;
            PgnBookReader bookReader = new PgnBookReader(pgnStream);
            PgnGameModel pgnGameModel;

            while ((pgnGameModel = bookReader.readGame()) != null) {
                ChessGame importedGame = chessGameService.batchImport(pgnGameModel);
                if (importedGame != null) {
                    importCount += 1;
                    chessPositionService.updateGamePositions(importedGame.getId());
                }

                logger.info("{} game {}: {} vs {}",
                        file.getName(),
                        importCount,
                        pgnGameModel.getWhitePlayerName(),
                        pgnGameModel.getBlackPlayerName());

            }

            performanceMonitor.register("PgnImportProcessor", "import", importCount, "game");
            logger.info("Imported {} game(s) from {}", importCount, file);
        } catch (IOException e) {
            throw new IllegalStateException("File processing failed on " + file, e);
        }

        return file;
    }
}
