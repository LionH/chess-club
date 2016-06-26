package org.chesscorp.club.service;

import org.alcibiade.chess.model.ChessException;
import org.alcibiade.chess.model.ChessMovePath;
import org.alcibiade.chess.model.ChessPosition;
import org.alcibiade.chess.persistence.PgnMarshaller;
import org.alcibiade.chess.persistence.PositionMarshaller;
import org.alcibiade.chess.rules.ChessHelper;
import org.alcibiade.chess.rules.ChessRules;
import org.chesscorp.club.dto.ChessAnalysis;
import org.chesscorp.club.dto.ChessAnalysisMove;
import org.chesscorp.club.model.game.ChessGame;
import org.chesscorp.club.model.game.ChessMove;
import org.chesscorp.club.model.stats.ChessClubPosition;
import org.chesscorp.club.model.stats.ChessMoveToPosition;
import org.chesscorp.club.persistence.ChessGameRepository;
import org.chesscorp.club.persistence.ChessMoveRepository;
import org.chesscorp.club.persistence.ChessMoveToPositionRepository;
import org.chesscorp.club.persistence.ChessPositionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Position management implementations.
 */
@Component
public class ChessPositionServiceImpl implements ChessPositionService {
    private static final List<ChessGame> EMPTY_GAMES_LIST = new ArrayList<>();
    private Logger logger = LoggerFactory.getLogger(ChessPositionServiceImpl.class);
    @Autowired
    private ChessPositionRepository chessPositionRepository;
    @Autowired
    private ChessGameRepository chessGameRepository;
    @Autowired
    private ChessMoveToPositionRepository chessMoveToPositionRepository;
    @Autowired
    private ChessMoveRepository chessMoveRepository;
    @Autowired
    private ChessRules chessRules;
    @Autowired
    private PgnMarshaller pgnMarshaller;
    @Autowired
    private MessagingService messagingService;
    @Autowired
    @Qualifier("fixed")
    private PositionMarshaller positionMarshaller;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public long updateMovePositions() {
        logger.debug("Updating position tables");

        ChessMoveToPosition lastProcessedMove = chessMoveToPositionRepository.findFirstByOrderByChessMoveIdDesc();
        logger.debug("Last processed move is {}", lastProcessedMove);

        long lastMoveId = 0;

        if (lastProcessedMove != null) {
            lastMoveId = lastProcessedMove.getChessMoveId();
        }

        long movesProcessedCount = chessMoveRepository.findAllByIdGreaterThan(lastMoveId).map(m -> {
            ChessGame game = m.getGame();

            try {
                ChessPosition position = chessRules.getInitialPosition();
                for (ChessMove move : game.getMoves()) {
                    ChessMovePath path = pgnMarshaller.convertPgnToMove(position, move.getPgn());
                    position = ChessHelper.applyMoveAndSwitch(chessRules, position, path);

                    if (move.getId().equals(m.getId())) {
                        break;
                    }
                }

                String positionText = positionMarshaller.convertPositionToString(position);
                logger.trace("Processing move {} - {}", m.getId(), positionText);

                ChessClubPosition clubPosition = chessPositionRepository.findOneByText(positionText);
                if (clubPosition == null) {
                    clubPosition = chessPositionRepository.saveAndFlush(new ChessClubPosition(positionText));
                    messagingService.notifyPositionCreated(clubPosition.getId());
                }

                chessMoveToPositionRepository.saveAndFlush(new ChessMoveToPosition(m.getId(), clubPosition));

                return m;
            } catch (ChessException chessEx) {
                throw new IllegalStateException("Failed to parse game " + game.getId(), chessEx);
            }
        }).collect(Collectors.counting());

        return movesProcessedCount;
    }

    @Override
    @Transactional
    public long updateGamePositions(Number gameId) {
        logger.debug("Updating position for game {}", gameId);

        ChessGame game = chessGameRepository.getOne(gameId);

        try {
            long updates = 0;
            ChessPosition position = chessRules.getInitialPosition();
            for (ChessMove move : game.getMoves()) {
                ChessMovePath path = pgnMarshaller.convertPgnToMove(position, move.getPgn());
                position = ChessHelper.applyMoveAndSwitch(chessRules, position, path);

                ChessMoveToPosition moveToPosition = chessMoveToPositionRepository.findOne(move.getId());
                if (moveToPosition == null) {
                    String positionText = positionMarshaller.convertPositionToString(position);
                    ChessClubPosition clubPosition = chessPositionRepository.findOneByText(positionText);
                    if (clubPosition == null) {
                        clubPosition = chessPositionRepository.saveAndFlush(new ChessClubPosition(positionText));
                        messagingService.notifyPositionCreated(clubPosition.getId());
                    }

                    chessMoveToPositionRepository.saveAndFlush(new ChessMoveToPosition(move.getId(), clubPosition));
                    updates += 1;
                }
            }

            return updates;
        } catch (ChessException chessEx) {
            throw new IllegalStateException("Failed to parse game " + game.getId(), chessEx);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChessGame> findRelatedGames(Number gameId) {
        ChessGame game = chessGameRepository.findOne(gameId.longValue());
        if (game == null) {
            return EMPTY_GAMES_LIST;
        }

        List<ChessMove> moves = game.getMoves();
        if (moves.isEmpty()) {
            return EMPTY_GAMES_LIST;
        }

        ChessMove lastMove = moves.get(moves.size() - 1);
        ChessMoveToPosition moveToPosition = chessMoveToPositionRepository.getOne(lastMove.getId());
        ChessClubPosition position = moveToPosition.getChessPosition();

        List<ChessGame> relatedGames = chessMoveToPositionRepository
                .findFirst10ByChessClubPositionId(position.getId()).stream()
                .map(mtp -> chessMoveRepository.getOne(mtp.getChessMoveId()))
                .filter(move -> !move.getGame().getId().equals(game.getId()))
                .map(ChessMove::getGame)
                .collect(Collectors.toList());

        return relatedGames;
    }

    @Override
    @Transactional
    public ChessClubPosition findOrCreatePosition(String positionText) {
        ChessClubPosition position = chessPositionRepository.findOneByText(positionText);

        if (position == null) {
            position = chessPositionRepository.save(new ChessClubPosition(positionText));
            messagingService.notifyPositionCreated(position.getId());
        }

        return position;
    }

    @Override
    @Transactional(readOnly = true)
    public ChessAnalysis getGameAnalysis(Number gameId) {
        ChessGame game = chessGameRepository.findOne(gameId.longValue());
        if (game == null) {
            return null;
        }

        List<ChessAnalysisMove> movesAnalysis = IntStream.range(0, game.getMoves().size())
                .mapToObj(moveIndex -> {
                    ChessMove move = game.getMoves().get(moveIndex);
                    ChessMoveToPosition moveToPosition = chessMoveToPositionRepository.findOne(move.getId());
                    ChessClubPosition chessPosition = moveToPosition.getChessPosition();

                    if (chessPosition == null || chessPosition.getScore() == null) {
                        return null;
                    }

                    ChessAnalysisMove moveAnalysis = new ChessAnalysisMove(
                            move.getId(),
                            moveIndex,
                            chessPosition.getScore(),
                            chessPosition.getExpected()
                    );

                    return moveAnalysis;
                })
                .filter(ma -> (ma != null))
                .collect(Collectors.toList());

        return new ChessAnalysis(game.getId(), movesAnalysis);
    }
}
