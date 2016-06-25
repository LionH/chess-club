package org.chesscorp.club.service;

import org.alcibiade.chess.engine.ChessEngineAnalyticalController;
import org.alcibiade.chess.engine.EngineAnalysisReport;
import org.chesscorp.club.model.game.ChessGame;
import org.chesscorp.club.model.game.ChessMove;
import org.chesscorp.club.model.stats.ChessClubPosition;
import org.chesscorp.club.persistence.ChessMoveRepository;
import org.chesscorp.club.persistence.ChessMoveToPositionRepository;
import org.chesscorp.club.persistence.ChessPositionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Engine analysis features.
 */
@Component
public class ChessAnalysisServiceImpl implements ChessAnalysisService {

    private Logger logger = LoggerFactory.getLogger(ChessAnalysisServiceImpl.class);

    @Autowired
    private ChessPositionRepository positionRepository;
    @Autowired
    private ChessMoveToPositionRepository moveToPositionRepository;
    @Autowired
    private ChessEngineAnalyticalController analyticalController;
    @Autowired
    private ChessMoveRepository moveRepository;

    @Override
    @Transactional
    public void analyzePosition(long positionId) {
        ChessClubPosition position = positionRepository.getOne(positionId);
        logger.debug("Analyzing position {}", position);
        Long moveId = moveToPositionRepository.findFirst10ByChessClubPositionId(positionId).get(0).getChessMoveId();
        ChessMove move = moveRepository.getOne(moveId);
        ChessGame game = move.getGame();

        logger.debug("Position found in game {}", game);

        List<String> moves = new ArrayList<>();

        for (ChessMove moveIt : game.getMoves()) {
            moves.add(moveIt.getPgn());

            if (moveId.equals(moveIt.getId())) {
                break;
            }
        }

        logger.debug("Analyzing moves {}", moves);

        /* Engine is providing analysis based on the current player. In the database, we want to keep all scores
           relative to white player. */
        int multiplier = (moves.size() % 2 == 0) ? 1 : -1;

        EngineAnalysisReport analysisResult = analyticalController.analyze(moves);
        position.setScore(multiplier * analysisResult.getPositionScore());
        position.setExpected(analysisResult.getExpectedMoves().stream().collect(Collectors.joining(" ")));
        positionRepository.saveAndFlush(position);

        logger.debug("Analyzed position is {}", position);
    }
}
