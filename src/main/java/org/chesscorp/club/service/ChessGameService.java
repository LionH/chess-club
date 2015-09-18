package org.chesscorp.club.service;

import org.chesscorp.club.model.ChessGame;

import java.io.IOException;
import java.io.InputStream;

public interface ChessGameService {

    ChessGame createGame(Number whitePlayer, Number blackPlayer);

    ChessGame getGame(Number id);

    ChessGame move(ChessGame game, String pgnMove);

    void batchImport(InputStream pgnStream) throws IOException;
}
