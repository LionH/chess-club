package org.chesscorp.club.jobs;

import org.chesscorp.club.monitoring.PerformanceMonitor;
import org.chesscorp.club.service.ChessAnalysisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Game update post-processing.
 */
@Component
@Profile("ai-gnuchess")
public class ChessPositionAnalysisListener {
    private Logger logger = LoggerFactory.getLogger(ChessPositionAnalysisListener.class);

    private ChessAnalysisService chessAnalysisService;
    private PerformanceMonitor performanceMonitor;

    @Autowired
    public ChessPositionAnalysisListener(ChessAnalysisService chessAnalysisService,
                                         PerformanceMonitor performanceMonitor) {
        this.chessAnalysisService = chessAnalysisService;
        this.performanceMonitor = performanceMonitor;
    }

    @JmsListener(destination = "chess-position-created")
    @Transactional
    public void analyzePosition(Long positionId) {
        logger.debug("Analyzing position {}", positionId);
        performanceMonitor.mark();
        chessAnalysisService.analyzePosition(positionId);
        performanceMonitor.register("ChessPositionAnalysisListener", "analyze-position", 1, "position");
    }
}
