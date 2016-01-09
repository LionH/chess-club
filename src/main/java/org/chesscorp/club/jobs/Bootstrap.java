package org.chesscorp.club.jobs;


import org.chesscorp.club.service.BootstrapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Profile("bootstrap")
public class Bootstrap {

    @Autowired
    private BootstrapService bootstrapService;

    @PostConstruct
    public void init() {
        bootstrapService.populate();
        bootstrapService.fixPgnNotationInGames();
    }
}
