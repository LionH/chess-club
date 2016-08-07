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
public class MailServiceSmtp {
    private Logger logger = LoggerFactory.getLogger(MailServiceSmtp.class);

    private String sender;
    private String recipient;
    private JavaMailSender mailSender;

    @Autowired
    public MailServiceSmtp(JavaMailSender mailSender,
                           @Value("${pan-discovery.reports.recipient}") String recipient,
                           @Value("${pan-discovery.reports.sender}") String sender) {
        this.sender = sender;
        this.mailSender = mailSender;
        this.recipient = recipient;
    }

    public void sendAccountValidationToken() {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(this.recipient);
            helper.setFrom(sender);
            helper.setSubject("...");
            helper.setText("....");


            // FileSystemResource fileSystemResource = new FileSystemResource(reportFile);
            // helper.addAttachment(fileSystemResource.getFilename(), fileSystemResource);

            this.mailSender.send(message);
            logger.info("Reports sent to {}", recipient);
        } catch (MessagingException e) {
            throw new MailParseException(e);
        }
    }
}
