package org.chesscorp.club.service;

/**
 * Templating operations.
 */
public interface TemplateService {

    String buildEmailAccountValidation(String recipientName, String validationToken);
}
