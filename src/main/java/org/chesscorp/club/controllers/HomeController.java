package org.chesscorp.club.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Entry point for UI queries.
 *
 * @author Yannick Kirschhoffer <alcibiade@alcibiade.org>
 */
@Controller
@ApiIgnore
public class HomeController {

    @RequestMapping(value = "/")
    public String getHome() {
        return "forward:/club.html";
    }
}
