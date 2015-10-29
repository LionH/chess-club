package org.chesscorp.club.service;

import org.chesscorp.club.model.comment.ChessComment;
import org.chesscorp.club.model.people.Player;

import java.util.List;

/**
 * Publish and access comments.
 */

public interface ChessCommentService {
    List<ChessComment> getCommentsByGame(Long gameId);

    void postComment(Player player, Long gameId, String text);
}
