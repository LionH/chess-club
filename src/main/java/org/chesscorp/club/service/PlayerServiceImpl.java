package org.chesscorp.club.service;

import org.chesscorp.club.dto.PlayerProfile;
import org.chesscorp.club.model.game.EloRating;
import org.chesscorp.club.model.people.ClubPlayer;
import org.chesscorp.club.model.people.Player;
import org.chesscorp.club.model.people.RobotPlayer;
import org.chesscorp.club.persistence.ClubPlayerRepository;
import org.chesscorp.club.persistence.EloRatingRepository;
import org.chesscorp.club.persistence.PlayerRepository;
import org.chesscorp.club.persistence.RobotRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class PlayerServiceImpl implements PlayerService {
    private Logger logger = LoggerFactory.getLogger(PlayerServiceImpl.class);

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private RobotRepository robotRepository;

    @Autowired
    private ClubPlayerRepository clubPlayerRepository;

    @Autowired
    private EloRatingRepository eloRatingRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Player> search(String query) {
        return this.search(query, null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Player> search(String query, Integer limit) {
        String processedQuery = query.toLowerCase();
        List<Player> result = new ArrayList<>();

        /*
         * Yes, this is a quite lame approach, and let's find some time for an index-based approach soon !
         */
        Stream<Player> playerStream = playerRepository.streamAllPlayers();

        playerStream.filter(player -> {
            String displayName = player.getDisplayName().toLowerCase();
            return displayName.contains(processedQuery);
        }).forEach(player -> {
            if (limit == null || result.size() < limit) {
                result.add(player);
            }
        });

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public PlayerProfile getProfile(Long playerId) {
        Player player = playerRepository.getOne(playerId);
        List<EloRating> history = eloRatingRepository.findByPlayerId(playerId);
        PlayerProfile profile = new PlayerProfile(player, history);
        return profile;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RobotPlayer> searchAI() {
        List<RobotPlayer> robots = robotRepository.findAll();
        return robots;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClubPlayer> searchOpponents(Long playerId) {
        return clubPlayerRepository
                .findAllByActive(true)
                .filter(p -> p.getId().longValue() != playerId.longValue())
                .collect(Collectors.toList());
    }


}
