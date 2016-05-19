package org.chesscorp.club.utilities.hash;

import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;

/**
 * Hashing operations specific to Gravatar management.
 */
@Component
public class GravatarHashManager {

    public String hashGravatar(String email) {
        try {
            String processedMessage = email.toLowerCase().trim();
            byte[] bytesOfMessage = processedMessage.getBytes("UTF-8");
            return DigestUtils.md5DigestAsHex(bytesOfMessage);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("UTF-8 not supported ?!?!", e);
        }
    }
}
