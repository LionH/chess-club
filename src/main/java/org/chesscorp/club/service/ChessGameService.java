package org.chesscorp.club.service;

import org.chesscorp.club.model.ChessGame;

public interface ChessGameService {

    ChessGame createGame(String whitePlayer, String blackPlayer);
    ChessGame getGame(String id);
}
