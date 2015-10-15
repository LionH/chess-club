package org.chesscorp.club.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.config.EnableIntegration;

@Configuration
@EnableIntegration
@ImportResource("classpath:integration-pgnimport.xml")
@Profile("integration")
public class IntegrationConfig {

}