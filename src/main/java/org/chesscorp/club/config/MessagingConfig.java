package org.chesscorp.club.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public void setupActiveMQTrust(ActiveMQConnectionFactory activeMQConnectionFactory) {
        activeMQConnectionFactory.setTrustAllPackages(true);
    }
}
