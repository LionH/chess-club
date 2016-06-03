package org.chesscorp.club.service;

import org.alcibiade.chess.model.ChessMovePath;
import org.alcibiade.chess.model.ChessPosition;
import org.alcibiade.chess.model.PgnMoveException;
import org.alcibiade.chess.persistence.PgnBookReader;
import org.alcibiade.chess.persistence.PgnGameModel;
import org.alcibiade.chess.persistence.PgnMarshaller;
import org.alcibiade.chess.persistence.PositionMarshaller;
import org.alcibiade.chess.rules.ChessHelper;
import org.alcibiade.chess.rules.ChessRules;
import org.chesscorp.club.dto.ChessOpeningDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Openings search.
 */
@Component
public class ChessOpeningsServiceImpl implements ChessOpeningsService {

    private Logger logger = LoggerFactory.getLogger(ChessOpeningsServiceImpl.class);

    private OpeningNode openingNodes = new OpeningNode();

    private Map<String, OpeningNode> positionIndex;

    @Autowired
    private ChessRules chessRules;

    @Autowired
    private PgnMarshaller pgnMarshaller;

    @Autowired
    @Qualifier("fixed")
    private PositionMarshaller positionMarshaller;

    @PostConstruct
    public void init() throws IOException {
        positionIndex = new HashMap<>();

        InputStream stream = this.getClass().getResourceAsStream("ChessOpenings.pgn");
        PgnBookReader bookReader = new PgnBookReader(stream);
        PgnGameModel gameModel;
        int openingCount = 0;

        while ((gameModel = bookReader.readGame()) != null) {
            String openingName = gameModel.getWhitePlayerName();
            String extension = gameModel.getBlackPlayerName();

            if (!extension.equals("?")) {
                openingName += " - " + extension;
            }

            OpeningNode node = openingNodes;
            ChessPosition position = chessRules.getInitialPosition();

            try {
                for (String move : gameModel.getMoves()) {
                    ChessMovePath movePath = pgnMarshaller.convertPgnToMove(position, move);
                    position = ChessHelper.applyMoveAndSwitch(chessRules, position, movePath);
                    String positionText = positionMarshaller.convertPositionToString(position);

                    if (node.getNextMoves().containsKey(move)) {
                        node = node.getNextMoves().get(move);
                        positionIndex.put(positionText, node);
                    } else {
                        OpeningNode newNode = new OpeningNode();
                        node.getNextMoves().put(move, newNode);
                        node = newNode;
                        positionIndex.put(positionText, node);
                    }
                }
            } catch (PgnMoveException e) {
                logger.warn("Could not load opening game " + gameModel, e);
            }


            logger.trace("Opening {} registered as {}", openingName, gameModel.getMoves());

            node.setName(openingName);

            openingCount += 1;
        }

        logger.info("Loaded {} chess openings", openingCount);
    }

    @Override
    public List<ChessOpeningDescription> getOpenings(List<String> moves) {
        List<ChessOpeningDescription> openings = new ArrayList<>();
        ChessPosition position = chessRules.getInitialPosition();

        for (String move : moves) {
            ChessMovePath movePath = pgnMarshaller.convertPgnToMove(position, move);
            position = ChessHelper.applyMoveAndSwitch(chessRules, position, movePath);
        }

        String positionText = positionMarshaller.convertPositionToString(position);

        OpeningNode node = positionIndex.get(positionText);

        if (node != null && node != openingNodes) {
            openings.add(new ChessOpeningDescription(node.getName(), node.getNextMoves().keySet()));
        }

        return openings;
    }


    private static class OpeningNode {
        private String name;

        private Map<String, OpeningNode> nextMoves;

        public OpeningNode() {
            this.nextMoves = new TreeMap<>();
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Map<String, OpeningNode> getNextMoves() {
            return nextMoves;
        }
    }
}
