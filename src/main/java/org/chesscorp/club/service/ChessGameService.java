package org.chesscorp.club.service;

import org.alcibiade.chess.persistence.PgnGameModel;
import org.chesscorp.club.model.game.ChessGame;
import org.chesscorp.club.model.people.Player;

import java.util.List;

public interface ChessGameService {

    ChessGame createGame(Number whitePlayer, Number blackPlayer);

    ChessGame getGame(Number id);

    ChessGame move(ChessGame game, String pgnMove);

    /**
     * Search for games.
     *
     * @param playerId identifier of a player that is search both as white or black
     * @param open     if null, return all games, otherwise return games which are open/ended based on this flag
     * @return all the matching games
     */
    List<ChessGame> searchGames(Number playerId, Boolean open);

    /**
     * Import a single game from a PGN data stream.
     *
     * @param pgnGameModel the PGN game model
     * @return the number of imported games
     */
    long batchImport(PgnGameModel pgnGameModel);

    /**
     * Resign on an existing game. It is accounted as a loss if a complete turn has been played.
     *
     * @param game   the target game.
     * @param player the player resigning.
     * @return the updated game model.
     */
    ChessGame resign(ChessGame game, Player player);
}
