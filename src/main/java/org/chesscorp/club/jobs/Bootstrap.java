package org.chesscorp.club.jobs;


import org.chesscorp.club.service.BootstrapService;
import org.chesscorp.club.service.ChessPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Profile("bootstrap")
public class Bootstrap {

    private BootstrapService bootstrapService;
    private ChessPositionService chessPositionService;

    @Autowired
    public Bootstrap(BootstrapService bootstrapService, ChessPositionService chessPositionService) {
        this.bootstrapService = bootstrapService;
        this.chessPositionService = chessPositionService;
    }


    @PostConstruct
    public void init() {
        bootstrapService.populate();
        chessPositionService.updateMovePositions();
//        bootstrapService.fixPgnNotationInGames();
    }
}
