package org.chesscorp.club.persistence;

import org.chesscorp.club.model.comment.ChessComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChessCommentRepository extends JpaRepository<ChessComment, Number> {

    List<ChessComment> findByChessGameId(Long id);

}
