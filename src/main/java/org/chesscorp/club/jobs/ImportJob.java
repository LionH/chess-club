package org.chesscorp.club.jobs;


import org.chesscorp.club.service.ChessGameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
@Profile("jobs")
public class ImportJob {

    private Logger logger = LoggerFactory.getLogger(ImportJob.class);

    @Autowired
    private ChessGameService chessGameService;

    @Scheduled(fixedDelay = 10_000)
    public void pollImportFolder() throws IOException {
        logger.trace("Polling import folder");

        Path importFolderPath = Paths.get("import");

        if (!Files.isDirectory(importFolderPath)) {
            logger.trace("No import folder found");
            return;
        }

        Files.list(importFolderPath)
                .filter(p -> p.getFileName().toString().endsWith(".pgn"))
                .forEach(p -> {
                    logger.debug("Importing games from {}", p);

                    try (InputStream stream = Files.newInputStream(p)) {
                        chessGameService.batchImport(stream);

                        Path renamedPath = p.getParent().resolve(p.getFileName().toString() + ".processed");
                        logger.debug("Renaming file {} to {}", p, renamedPath);
                        Files.move(p, renamedPath, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
                        stream.close();
                    } catch (IOException e) {
                        throw new IllegalStateException("File processing failed on " + p, e);
                    }
                });
    }
}
