package org.chesscorp.club.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Chess related components initialization.
 */
@Configuration
@ComponentScan({"org.alcibiade.chess.engine", "org.alcibiade.chess.persistence", "org.alcibiade.chess.rules"})
public class ChessConfig {

}
