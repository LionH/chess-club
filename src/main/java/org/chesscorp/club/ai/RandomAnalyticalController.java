package org.chesscorp.club.ai;

import java.util.ArrayList;
import java.util.Collection;

import org.alcibiade.chess.engine.ChessEngineAnalyticalController;
import org.alcibiade.chess.engine.ChessEngineFailureException;
import org.alcibiade.chess.engine.EngineAnalysisReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RandomAnalyticalController implements ChessEngineAnalyticalController {

    @Autowired
    private RandomAI randomAI;

    @Override
    public String computeNextMove(int depth, int random, Collection<String> moves) throws ChessEngineFailureException {
        return randomAI.computeNextMove("", new ArrayList<>(moves));
    }

    @Override
    public EngineAnalysisReport analyze(Collection<String> moves) throws ChessEngineFailureException {
        return new EngineAnalysisReport(1, new ArrayList<>(moves));
    }

}
