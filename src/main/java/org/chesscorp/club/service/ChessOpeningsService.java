package org.chesscorp.club.service;

import org.chesscorp.club.dto.ChessOpeningDescription;

import java.util.List;

/**
 * Openings matching.
 */
public interface ChessOpeningsService {
    List<ChessOpeningDescription> getOpenings(List<String> moves);
}
