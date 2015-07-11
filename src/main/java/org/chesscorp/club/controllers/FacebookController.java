package org.chesscorp.club.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Facebook controller.
 *
 * @author Yannick Kirschhoffer <alcibiade@alcibiade.org>
 */
@Controller
@ApiIgnore
public class FacebookController {

    @RequestMapping(value = "/connect/facebook", method = RequestMethod.GET)
    public String connectToFacebook() {
        return "forward:/club.html";
    }
}
