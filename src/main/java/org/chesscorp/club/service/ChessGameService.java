package org.chesscorp.club.service;

import org.chesscorp.club.model.ChessGame;

import java.io.IOException;
import java.io.InputStream;

public interface ChessGameService {

    ChessGame createGame(String whitePlayer, String blackPlayer);

    ChessGame getGame(String id);

    ChessGame move(ChessGame game, String pgnMove);

    void batchImport(InputStream pgnStream) throws IOException;
}
