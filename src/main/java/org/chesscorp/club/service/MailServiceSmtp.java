package org.chesscorp.club.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailParseException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * A JavaMail SMTP sender implementation.
 */
@Component
@Profile("mail")
public class MailServiceSmtp implements MailService {
    private Logger logger = LoggerFactory.getLogger(MailServiceSmtp.class);

    private String sender;
    private JavaMailSender mailSender;
    private TemplateService templateService;

    @Autowired
    public MailServiceSmtp(JavaMailSender mailSender,
                           @Value("${chesscorp.mail.sender}") String sender,
                           TemplateService templateService) {
        this.sender = sender;
        this.mailSender = mailSender;
        this.templateService = templateService;
    }

    @Override
    public void sendAccountValidationLink(String recipientName, String recipientAddress, String validationToken) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(recipientAddress);
            helper.setFrom(sender);
            helper.setSubject("ChessCorp Account Validation");

            String htmlContent = templateService.buildEmailAccountValidation(recipientName, validationToken);
            helper.setText(htmlContent, true); // true = isHtml

            this.mailSender.send(message);
            logger.info("Account validation token sent to {}", recipientAddress);
        } catch (MessagingException e) {
            throw new MailParseException(e);
        }
    }
}
