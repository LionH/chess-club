package org.chesscorp.club.service;

import com.google.common.collect.Lists;
import org.alcibiade.chess.model.ChessGameStatus;
import org.chesscorp.club.dto.PlayerProfile;
import org.chesscorp.club.dto.PvpStatus;
import org.chesscorp.club.dto.PvpStatusItem;
import org.chesscorp.club.model.game.EloRating;
import org.chesscorp.club.model.people.ClubPlayer;
import org.chesscorp.club.model.people.Player;
import org.chesscorp.club.model.people.RobotPlayer;
import org.chesscorp.club.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class PlayerServiceImpl implements PlayerService {
    private Logger logger = LoggerFactory.getLogger(PlayerServiceImpl.class);

    private PlayerRepository playerRepository;
    private RobotRepository robotRepository;
    private ClubPlayerRepository clubPlayerRepository;
    private ChessGameRepository chessGameRepository;
    private EloRatingRepository eloRatingRepository;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository, RobotRepository robotRepository,
                             ClubPlayerRepository clubPlayerRepository, ChessGameRepository chessGameRepository,
                             EloRatingRepository eloRatingRepository) {
        this.playerRepository = playerRepository;
        this.robotRepository = robotRepository;
        this.clubPlayerRepository = clubPlayerRepository;
        this.chessGameRepository = chessGameRepository;
        this.eloRatingRepository = eloRatingRepository;
    }

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
        List<EloRating> history = eloRatingRepository.findByPlayerIdOrderByScoreDateAsc(playerId);
        Map<Player, PvpStatus> pvpStatistics = new HashMap<>();

        List<ChessGameStatus> closedStatus = Lists.newArrayList(
                ChessGameStatus.BLACKWON,
                ChessGameStatus.WHITEWON,
                ChessGameStatus.PAT
        );

        chessGameRepository.findByWhitePlayerIdAndStatusInOrBlackPlayerIdAndStatusIn(
                playerId, closedStatus, playerId, closedStatus
        ).forEach(game -> {
                    Player opponent = (game.getWhitePlayer().equals(player)) ? game.getBlackPlayer() : game.getWhitePlayer();
                    PvpStatus statistics = pvpStatistics.get(opponent);

                    if (statistics == null) {
                        statistics = new PvpStatus();
                        pvpStatistics.put(opponent, statistics);
                    }

                    switch (game.getStatus()) {
                        case WHITEWON:
                            if (player.equals(game.getWhitePlayer())) {
                                statistics.setWins(statistics.getWins() + 1);
                            } else {
                                statistics.setLosses(statistics.getLosses() + 1);
                            }
                            break;
                        case BLACKWON:
                            if (player.equals(game.getBlackPlayer())) {
                                statistics.setWins(statistics.getWins() + 1);
                            } else {
                                statistics.setLosses(statistics.getLosses() + 1);
                            }
                            break;
                        case PAT:
                            statistics.setDraws(statistics.getDraws() + 1);
                            break;
                    }
                }
        );

        List<PvpStatusItem> items = pvpStatistics.keySet().stream()
                .map(opponent -> new PvpStatusItem(opponent, pvpStatistics.get(opponent)))
                .collect(Collectors.toList());

        Collections.sort(items);

        PlayerProfile profile = new PlayerProfile(player, history, items);
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
