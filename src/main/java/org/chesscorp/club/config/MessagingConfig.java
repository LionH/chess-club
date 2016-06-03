package org.chesscorp.club.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.EnableJms;

/**
 * Enable and configure messaging.
 */
@Configuration
@EnableJms
@Profile("jobs")
public class MessagingConfig {
}
