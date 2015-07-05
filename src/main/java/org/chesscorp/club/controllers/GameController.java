package org.chesscorp.club.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Yannick Kirschhoffer alcibiade@alcibiade.org
 */
@RestController
public class GameController {

    @RequestMapping("/api/game/get")
    public Object getGame(String id) {
        return "hello";
    }
}
