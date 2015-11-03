package org.chesscorp.club.service;

import org.alcibiade.chess.model.ChessException;
import org.alcibiade.chess.model.ChessMovePath;
import org.alcibiade.chess.model.ChessPosition;
import org.alcibiade.chess.persistence.PgnMarshaller;
import org.alcibiade.chess.persistence.PositionMarshaller;
import org.alcibiade.chess.rules.ChessHelper;
import org.alcibiade.chess.rules.ChessRules;
import org.chesscorp.club.model.game.ChessGame;
import org.chesscorp.club.model.game.ChessMove;
import org.chesscorp.club.model.stats.ChessClubPosition;
import org.chesscorp.club.model.stats.ChessMoveToPosition;
import org.chesscorp.club.persistence.ChessMoveRepository;
import org.chesscorp.club.persistence.ChessMoveToPositionRepository;
import org.chesscorp.club.persistence.ChessPositionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Position management implementations.
 */
@Component
public class ChessPositionServiceImpl implements ChessPositionService {
    private Logger logger = LoggerFactory.getLogger(ChessPositionServiceImpl.class);

    @Autowired
    private ChessPositionRepository chessPositionRepository;
    @Autowired
    private ChessMoveToPositionRepository chessMoveToPositionRepository;
    @Autowired
    private ChessMoveRepository chessMoveRepository;
    @Autowired
    private ChessRules chessRules;
    @Autowired
    private PgnMarshaller pgnMarshaller;
    @Autowired
    private PositionMarshaller positionMarshaller;

    @Override
    @Transactional
    public void updateMovePositions() {
        logger.debug("Updating position tables");

        ChessMoveToPosition lastProcessedMove = chessMoveToPositionRepository.findFirstByOrderByChessMoveIdDesc();
        logger.debug("Last processed move is {}", lastProcessedMove);

        long lastMoveId = 0;

        if (lastProcessedMove != null) {
            lastMoveId = lastProcessedMove.getChessMoveId();
        }

        List<ChessMove> movesToProcess = chessMoveRepository.findAllByIdGreaterThan(lastMoveId);

        movesToProcess.stream().forEach(m -> {
            ChessGame game = m.getGame();

            ChessPosition position = chessRules.getInitialPosition();
            for (ChessMove move : game.getMoves()) {
                try {
                    ChessMovePath path = pgnMarshaller.convertPgnToMove(position, move.getPgn());
                    position = ChessHelper.applyMoveAndSwitch(chessRules, position, path);
                } catch (ChessException e) {
                    throw new IllegalStateException("Invalid move data", e);
                }

                if (move.getId().equals(m.getId())) {
                    break;
                }
            }

            String positionText = positionMarshaller.convertPositionToString(position);
            logger.trace("Processing move {} - {}", m.getId(), positionText);


            ChessClubPosition clubPosition = chessPositionRepository.findOneByText(positionText);
            if (clubPosition == null) {
                clubPosition = chessPositionRepository.save(new ChessClubPosition(positionText));
            }

            chessMoveToPositionRepository.save(new ChessMoveToPosition(m.getId(), clubPosition));
        });
    }
}
