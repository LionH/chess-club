package org.chesscorp.club.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import java.util.Locale;

/**
 * Thymeleaf implementation of template service operations.
 */
@Component
public class TemplateServiceImpl implements TemplateService {
    private SpringTemplateEngine templateEngine;
    private String baseUrl;

    @Autowired
    public TemplateServiceImpl(SpringTemplateEngine templateEngine,
                               @Value("${chesscorp.mail.baseUrl}") String baseUrl) {
        this.templateEngine = templateEngine;
        this.baseUrl = baseUrl;
    }

    @Override
    public String buildEmailAccountValidation(String recipientName, String validationToken) {
        Locale locale = Locale.getDefault();
        Context ctx = new Context(locale);
        ctx.setVariable("recipientName", recipientName);
        ctx.setVariable("baseUrl", baseUrl);
        ctx.setVariable("token", validationToken);

        return this.templateEngine.process("email-account-validation", ctx);
    }
}
