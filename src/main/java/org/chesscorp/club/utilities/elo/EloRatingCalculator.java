package org.chesscorp.club.utilities.elo;


import org.springframework.stereotype.Component;

/**
 * ELO rating calculator.
 * <p>
 * See https://en.wikipedia.org/wiki/Elo_rating_system
 * </p>
 */
@Component
public class EloRatingCalculator {
    public static final int K = 20;
    public static final int INITIAL_RATING = 1200;

    public int computeRatingDelta(int playerRating, int opponentRating, double score) {
        double expected = 1. / (1 + Math.pow(10, (opponentRating - playerRating) / 400.));
        double delta = K * (score - expected);
        return (int) Math.round(delta);
    }

}
