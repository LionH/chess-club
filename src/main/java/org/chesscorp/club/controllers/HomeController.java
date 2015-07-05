package org.chesscorp.club.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Entry point for UI queries.
 *
 * @author Yannick Kirschhoffer <alcibiade@alcibiade.org>
 */
@Controller
public class HomeController {

    @RequestMapping(value = "/")
    public String getHome() {
        return "forward:/club.html";
    }
}
