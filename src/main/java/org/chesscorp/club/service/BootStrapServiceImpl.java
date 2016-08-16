package org.chesscorp.club.service;

import org.alcibiade.chess.model.ChessMovePath;
import org.alcibiade.chess.model.ChessPosition;
import org.alcibiade.chess.persistence.PgnMarshaller;
import org.alcibiade.chess.rules.ChessHelper;
import org.alcibiade.chess.rules.ChessRules;
import org.chesscorp.club.model.game.ChessGame;
import org.chesscorp.club.model.game.ChessMove;
import org.chesscorp.club.model.people.Account;
import org.chesscorp.club.model.people.ClubPlayer;
import org.chesscorp.club.model.people.Player;
import org.chesscorp.club.model.people.RobotPlayer;
import org.chesscorp.club.persistence.*;
import org.chesscorp.club.utilities.hash.HashManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Initialization operations.
 */
@Component
public class BootStrapServiceImpl implements BootstrapService {

    private Logger logger = LoggerFactory.getLogger(BootStrapServiceImpl.class);

    private PlayerRepository playerRepository;
    private RobotRepository robotRepository;
    private ChessGameRepository chessGameRepository;
    private ChessMoveRepository chessMoveRepository;
    private AccountRepository accountRepository;
    private HashManager hashManager;
    private ChessRules chessRules;
    private PgnMarshaller pgnMarshaller;
    private Environment environment;

    @Autowired
    public BootStrapServiceImpl(PlayerRepository playerRepository, RobotRepository robotRepository,
                                ChessGameRepository chessGameRepository, ChessMoveRepository chessMoveRepository,
                                AccountRepository accountRepository, HashManager hashManager, ChessRules chessRules,
                                PgnMarshaller pgnMarshaller, Environment environment) {
        this.playerRepository = playerRepository;
        this.robotRepository = robotRepository;
        this.chessGameRepository = chessGameRepository;
        this.chessMoveRepository = chessMoveRepository;
        this.accountRepository = accountRepository;
        this.hashManager = hashManager;
        this.chessRules = chessRules;
        this.pgnMarshaller = pgnMarshaller;
        this.environment = environment;
    }

    @Override
    @Transactional
    public void populate() {
        long playerCount = playerRepository.count();
        long gameCount = chessGameRepository.count();
        long accountCount = accountRepository.count();

        logger.info("Found {} accounts, {} players, {} games", accountCount, playerCount, gameCount);

        if (playerCount == 0 && gameCount == 0 && accountCount == 0) {
            logger.info("Creating sample robots");
            robotRepository.save(new RobotPlayer("Simple AI", "randomAI", ""));

            for (int l = 1; l < 8; l++) {
                robotRepository.save(new RobotPlayer("GnuChess Level " + l, "gnuchessAI", Integer.toString(l), true));
                robotRepository.save(new RobotPlayer("Phalanx Level " + l, "phalanxAI", Integer.toString(l), true));
                if (environment.acceptsProfiles("ai-crafty")) {
                    // This should only occur if the ai-crafty profile is set
                    robotRepository.save(new RobotPlayer("Crafty Level " + l, "craftyAI", Integer.toString(l), true));
                }
            }

            logger.info("Creating sample players");
            Player alcibiade = playerRepository.save(new ClubPlayer("Alcibiade"));
            Player john = playerRepository.save(new ClubPlayer("John"));
            Player bob = playerRepository.save(new ClubPlayer("Bob"));
            Player steve = playerRepository.save(new ClubPlayer("Steve"));

            logger.info("Creating sample games");
            chessGameRepository.save(new ChessGame(playerRepository.getOne(john.getId()), playerRepository.getOne(bob.getId())));
            chessGameRepository.save(new ChessGame(playerRepository.getOne(alcibiade.getId()), playerRepository.getOne(bob.getId())));

            logger.info("Creating sample accounts");
            String salt = hashManager.createSalt();
            accountRepository.save(new Account("alcibiade", salt, hashManager.hash(salt, "toto"), alcibiade));
            accountRepository.save(new Account("john", salt, hashManager.hash(salt, "john"), john));
            accountRepository.save(new Account("bob", salt, hashManager.hash(salt, "bob"), bob));
            accountRepository.save(new Account("steve", salt, hashManager.hash(salt, "steve"), steve));
        }
    }

    @Override
    @Transactional
    public void fixPgnNotationInGames() {
        chessGameRepository.findAll().forEach(g -> {
            ChessPosition position = chessRules.getInitialPosition();

            for (ChessMove move : g.getMoves()) {
                logger.debug("Checking pgn validity for game {}: {} vs {}",
                        g.getId(),
                        g.getWhitePlayer().getDisplayName(),
                        g.getBlackPlayer().getDisplayName());

                String originalPgn = move.getPgn();
                ChessMovePath movePath = pgnMarshaller.convertPgnToMove(position, originalPgn);

                String canonicalPgn = pgnMarshaller.convertMoveToPgn(position, movePath);

                if (!canonicalPgn.equals(originalPgn)) {
                    move.setPgn(canonicalPgn);
                    chessMoveRepository.save(move);
                    logger.warn("Game {}, move {} updated to {}", g.getId(), originalPgn, canonicalPgn);
                }

                position = ChessHelper.applyMoveAndSwitch(chessRules, position, movePath);
            }
        });
    }
}
