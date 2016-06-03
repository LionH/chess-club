package org.chesscorp.club.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Configuration for scheduled jobs.
 */
@Configuration
@EnableScheduling
@Profile("jobs")
public class JobsConfig {

}
