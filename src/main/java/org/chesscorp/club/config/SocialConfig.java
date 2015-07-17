package org.chesscorp.club.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.social.config.annotation.EnableSocial;

/**
 * Created by yk on 17/07/15.
 */
@Configuration
@EnableSocial
@Profile("facebook")
public class SocialConfig {
}
