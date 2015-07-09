package org.chesscorp.club.service;

import org.chesscorp.club.model.Player;

public interface PlayerService {

    void register(Player player);

    Player get(String playerId);
}
