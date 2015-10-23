package org.chesscorp.club.utilities.elo;

import org.springframework.stereotype.Component;

/**
 * ELO rating calculator.
 */
@Component
public class EloRatingCalculator {

    public static final int K = 20;

    public int computeNewScore(int playerRating, int opponentRating, String score) {
        return playerRating;
    }

}
