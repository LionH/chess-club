package org.chesscorp.club.config;

import org.alcibiade.chess.persistence.PgnMarshaller;
import org.alcibiade.chess.persistence.PgnMarshallerImpl;
import org.alcibiade.chess.persistence.PositionMarshaller;
import org.alcibiade.chess.persistence.PositionMarshallerImpl;
import org.alcibiade.chess.rules.ChessRules;
import org.alcibiade.chess.rules.ChessRulesImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Chess related components initialization.
 */
@Configuration
@ComponentScan("org.alcibiade.chess.engine")
public class ChessConfig {

    @Bean
    public ChessRules getChessRules() {
        ChessRulesImpl rules = new ChessRulesImpl();
        return rules;
    }

    @Bean
    public PgnMarshaller getPgnMarshaller() {
        PgnMarshallerImpl marshaller = new PgnMarshallerImpl();
        return marshaller;
    }

    @Bean
    public PositionMarshaller getPositionMarshaller() {
        PositionMarshaller marshaller = new PositionMarshallerImpl();
        return marshaller;
    }

//    @Bean
//    public GnuChessEngineImpl getGnuChessEngine() {
//        return new GnuChessEngineImpl();
//    }
}
