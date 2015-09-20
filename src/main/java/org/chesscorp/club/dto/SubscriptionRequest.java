package org.chesscorp.club.dto;

/**
 * Subscription data.
 */
public class SubscriptionRequest {

    private String displayName;
    private String email;
    private String password;

    public SubscriptionRequest() {
    }

    public SubscriptionRequest(String displayName, String email, String password) {
        this.displayName = displayName;
        this.email = email;
        this.password = password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "SubscriptionRequest{" +
                "displayName='" + displayName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
