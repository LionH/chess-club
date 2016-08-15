package org.chesscorp.club.service;

/**
 * Mail notification service interface.
 */
public interface MailService {

    void sendAccountValidationLink(String recipientName, String recipientAddress, String validationToken);
}
