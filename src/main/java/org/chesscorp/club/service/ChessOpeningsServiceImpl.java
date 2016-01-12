package org.chesscorp.club.service;

import org.alcibiade.chess.persistence.PgnBookReader;
import org.alcibiade.chess.persistence.PgnGameModel;
import org.chesscorp.club.dto.ChessOpeningDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Openings search.
 */
@Component
public class ChessOpeningsServiceImpl implements ChessOpeningsService {

    private Logger logger = LoggerFactory.getLogger(ChessOpeningsServiceImpl.class);

    private OpeningNode openingNodes = new OpeningNode();

    @PostConstruct
    public void init() throws IOException {
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

            for (String move : gameModel.getMoves()) {
                if (node.getNextMoves().containsKey(move)) {
                    node = node.getNextMoves().get(move);
                } else {
                    OpeningNode newNode = new OpeningNode();
                    node.getNextMoves().put(move, newNode);
                    node = newNode;
                }
            }

            logger.debug("Opening {} registered as {}", openingName, gameModel.getMoves());

            node.setName(openingName);

            openingCount += 1;
        }

        logger.info("Loaded {} chess openings", openingCount);
    }

    @Override
    public List<ChessOpeningDescription> getOpenings(List<String> moves) {
        List<ChessOpeningDescription> openings = new ArrayList<>();

        OpeningNode node = openingNodes;

        for (String move : moves) {
            node = node.getNextMoves().get(move);

            logger.debug("Move {} matches {}", move, node);

            if (node == null) {
                break;
            }
        }

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
