package org.chesscorp.club.service;

import org.chesscorp.club.model.ChessGame;
import org.chesscorp.club.model.Player;
import org.chesscorp.club.persistence.ChessGameRepository;
import org.chesscorp.club.persistence.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChessGameServiceImpl implements ChessGameService {

    @Autowired
    private ChessGameRepository chessGameRepository;
    @Autowired
    private PlayerRepository playerRepository;


    @Override
    public ChessGame createGame(String whitePlayer, String blackPlayer) {
        Player white = playerRepository.getOne(whitePlayer);
        Player black = playerRepository.getOne(blackPlayer);

        if (white.equals(black)) {
            throw new IllegalStateException("Can't open a game with a single player on both sides");
        }

        ChessGame game = new ChessGame(white, black);
        return chessGameRepository.save(game);
    }

    @Override
    public ChessGame getGame(String id) {
        return chessGameRepository.getOne(id);
    }
}
