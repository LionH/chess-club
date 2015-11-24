package org.chesscorp.club.service.factories;

import org.chesscorp.club.model.people.ExternalPlayer;
import org.chesscorp.club.persistence.PlayerRepository;
import org.chesscorp.club.utilities.normalize.TextNormalizer;
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

    @Autowired
    private TextNormalizer textNormalizer;

    @Override
    @Transactional
    public ExternalPlayer findOrCreateExternalPlayer(String displayName) {
        String normalizedName = textNormalizer.normalize(displayName);
        List<ExternalPlayer> players = playerRepository.findByNormalizedName(normalizedName);
        ExternalPlayer result;

        if (players.isEmpty()) {
            ExternalPlayer player = new ExternalPlayer(displayName, normalizedName);
            result = playerRepository.save(player);
        } else {
            result = players.get(0);
        }

        return result;
    }
}
