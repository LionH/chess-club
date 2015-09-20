package org.chesscorp.club.dto;

/**
 * Authentication request.
 */
public class AuthenticationRequest {

    private String email;

    private String password;

    public AuthenticationRequest() {
    }

    public AuthenticationRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "AuthenticationRequest{" + "email='" + email + "\'" + ", password not displayed }";
    }
}
