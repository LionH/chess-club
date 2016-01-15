package org.chesscorp.club.service;

import org.chesscorp.club.model.comment.ChessComment;
import org.chesscorp.club.model.game.ChessGame;
import org.chesscorp.club.model.people.Player;
import org.chesscorp.club.persistence.ChessCommentRepository;
import org.chesscorp.club.persistence.ChessGameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Component
public class ChessCommentServiceImpl implements ChessCommentService {
    private Logger logger = LoggerFactory.getLogger(ChessCommentServiceImpl.class);

    @Autowired
    private ChessCommentRepository chessCommentRepository;

    @Autowired
    private ChessGameRepository chessGameRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ChessComment> getCommentsByGame(Long gameId) {
        return chessCommentRepository.findByChessGameId(gameId);
    }

    @Override
    @Transactional
    public void postComment(Player player, Long gameId, String text) {
        ChessGame game = chessGameRepository.getOne(gameId);
        chessCommentRepository.save(new ChessComment(player, game, OffsetDateTime.now(), text));
    }
}
