package org.chesscorp.club.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * Game update post-processing.
 */
@Component
public class GameUpdateListener {
    private Logger logger = LoggerFactory.getLogger(GameUpdateListener.class);

    @JmsListener(destination = "chess-game-update")
    public void gameUpdated(String message) {
        logger.debug("Game {} updated", message);
    }
}
