package org.chesscorp.club.config;

import org.alcibiade.chess.engine.CraftyEngineImpl;
import org.alcibiade.chess.engine.GnuChessEngineImpl;
import org.alcibiade.chess.engine.PhalanxEngineImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Chess related components initialization.
 */
@Configuration
@ComponentScan({"org.alcibiade.chess.persistence", "org.alcibiade.chess.rules", "org.alcibiade.chess.engine.process"})
public class ChessConfig {

    @Bean
    public GnuChessEngineImpl getGnuChessEngine() {
        return new GnuChessEngineImpl();
    }

    @Bean
    public PhalanxEngineImpl getPhalanxEngine() {
        return new PhalanxEngineImpl();
    }

    @Bean
    @Profile("ai-crafty")
    public CraftyEngineImpl getCraftyEngine() {
        return new CraftyEngineImpl();
    }

}
