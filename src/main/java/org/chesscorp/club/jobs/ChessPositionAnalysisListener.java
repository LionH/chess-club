package org.chesscorp.club.jobs;

import org.chesscorp.club.monitoring.PerformanceMonitor;
import org.chesscorp.club.service.ChessAnalysisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Game update post-processing.
 */
@Component
public class ChessPositionAnalysisListener {
    private Logger logger = LoggerFactory.getLogger(ChessPositionAnalysisListener.class);

    @Autowired
    private ChessAnalysisService chessAnalysisService;

    @Autowired
    private PerformanceMonitor performanceMonitor;

    @JmsListener(destination = "chess-position-created")
    @Transactional
    public void analyzePosition(Long positionId) {
        logger.debug("Analyzing position {}", positionId);
        performanceMonitor.mark();
        chessAnalysisService.analyzePosition(positionId);
        performanceMonitor.register("ChessPositionAnalysisListener", "analyze-position", 1, "position");
    }
}
