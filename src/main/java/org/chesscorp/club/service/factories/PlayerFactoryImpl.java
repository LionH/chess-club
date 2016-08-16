package org.chesscorp.club.service.factories;

import org.chesscorp.club.model.people.ExternalPlayer;
import org.chesscorp.club.persistence.PlayerRepository;
import org.chesscorp.club.utilities.normalize.TextNormalizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

/**
 * Access the player repository and create new instances on demand.
 */
@Component
public class PlayerFactoryImpl implements PlayerFactory {

    private PlayerRepository playerRepository;
    private TextNormalizer textNormalizer;

    @Autowired
    public PlayerFactoryImpl(PlayerRepository playerRepository, TextNormalizer textNormalizer) {
        this.playerRepository = playerRepository;
        this.textNormalizer = textNormalizer;
    }

    @Override
    @Transactional
    public ExternalPlayer findOrCreateExternalPlayer(String displayName) {
        int columnIndex = displayName.indexOf(':');

        if (columnIndex > 0) {
            displayName = displayName.substring(0, columnIndex);
        }

        String normalizedName = textNormalizer.normalize(displayName);
        ExternalPlayer player = playerRepository.findOneByNormalizedName(normalizedName);

        if (player == null) {
            player = playerRepository.save(new ExternalPlayer(displayName, normalizedName));
        }

        return player;
    }
}
