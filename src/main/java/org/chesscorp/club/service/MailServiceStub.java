package org.chesscorp.club.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Stub for environments not having access to an actual mail server.
 */
@Component
@Profile("!mail")
public class MailServiceStub implements MailService {

    private Logger logger = LoggerFactory.getLogger(MailServiceStub.class);

    @PostConstruct
    public void init() {
        logger.warn("Mail service disabled.");
    }

    @Override
    public void sendAccountValidationLink(String recipientName, String recipientAddress, String validationToken) {
        logger.debug("Stubbed sending account validation link to {} / {} with token {}",
                recipientName, recipientAddress, validationToken);
    }
}
