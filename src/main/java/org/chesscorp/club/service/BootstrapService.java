package org.chesscorp.club.service;

/**
 * System initialization operations.
 */
public interface BootstrapService {

    void populate();

    void fixPgnNotationInGames();
}
