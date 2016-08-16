package org.chesscorp.club.controllers;

import org.chesscorp.club.dto.ReleaseInformation;
import org.chesscorp.club.service.ServerStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Server status information controller.
 *
 * @author Yannick Kirschhoffer alcibiade@alcibiade.org
 */
@RestController
@RequestMapping("/api/status")
public class StatusController {
    private ServerStatusService serverStatusService;

    @Autowired
    public StatusController(ServerStatusService serverStatusService) {
        this.serverStatusService = serverStatusService;
    }

    @Transactional(readOnly = true)
    @RequestMapping(value = "/release", method = RequestMethod.GET)
    public ReleaseInformation getRelease() {
        return serverStatusService.getReleaseInformation();
    }
}
