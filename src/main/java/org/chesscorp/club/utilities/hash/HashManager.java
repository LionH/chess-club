package org.chesscorp.club.utilities.hash;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

/**
 * Hash management for password management.
 */
@Component
public class HashManager {
    @Value("${password.salt.bytes:24}")
    private int saltBytes;

    @Value("${password.hash.bytes:24}")
    private int hashBytes;

    @Value("${password.pbkdf2.iterations:10000}")
    private int PBKDF2Iterations;

    private SecretKeyFactory skf;

    @PostConstruct
    public void init() throws NoSuchAlgorithmException {
        this.skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    }

    public String createSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[saltBytes];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public String hash(String salt, String text) {
        char[] textChars = text.toCharArray();
        byte[] saltBytes = Base64.getDecoder().decode(salt);
        PBEKeySpec spec = new PBEKeySpec(textChars, saltBytes, PBKDF2Iterations, hashBytes * 8);
        try {
            byte[] hash = skf.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (InvalidKeySpecException e) {
            throw new IllegalStateException("Hash operation failed", e);
        }
    }

}
