package org.chesscorp.club.config;

import org.alcibiade.chess.persistence.PgnMarshaller;
import org.alcibiade.chess.persistence.PgnMarshallerImpl;
import org.alcibiade.chess.rules.ChessRules;
import org.alcibiade.chess.rules.ChessRulesImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by yk on 20/07/15.
 */
@Configuration
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
}
