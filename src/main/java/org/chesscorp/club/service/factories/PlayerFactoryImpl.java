package org.chesscorp.club.service.factories;

import org.chesscorp.club.model.people.Player;
import org.chesscorp.club.persistence.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Access the player repository and create new instances on demand.
 */
@Component
public class PlayerFactoryImpl implements PlayerFactory {

    @Autowired
    private PlayerRepository playerRepository;

    @Override
    @Transactional
    public Player findOrCreatePlayer(String displayName) {
        List<Player> players = playerRepository.findByDisplayName(displayName);
        Player result;

        if (players.isEmpty()) {
            Player player = new Player(displayName);
            result = playerRepository.save(player);
        } else {
            result = players.get(0);
        }

        return result;
    }
}
