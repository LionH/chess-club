package org.chesscorp.club.service;

import org.chesscorp.club.model.Player;
import org.chesscorp.club.persistence.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    @Override
    @Transactional
    public void register(Player player) {
        playerRepository.save(player);
    }

    @Override
    @Transactional
    public Player get(String playerId) {
        return playerRepository.getOne(playerId);
    }


}
