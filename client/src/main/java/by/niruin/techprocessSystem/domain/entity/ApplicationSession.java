package by.niruin.techprocessSystem.domain.entity;

import by.niruin.techprocessSystem.exception.TokensUpdationException;
import by.niruin.techprocessSystem.ui.controller.Cleanable;
import org.springframework.stereotype.Component;

@Component
public class ApplicationSession implements Cleanable {
    private String accessToken;
    private String refreshToken;
    private User user;
    private SessionState state = SessionState.NOT_AUTHORIZED;

    @Override
    public synchronized void clear() {
        this.accessToken = null;
        this.refreshToken = null;
        this.user = null;
        this.state = SessionState.NOT_AUTHORIZED;
    }

    public synchronized void updateTokens(String access, String refresh) {
        if (this.state == SessionState.NOT_AUTHORIZED) {
            throw new TokensUpdationException("Tokens cannot be renewed to an unauthorized user");
        }

        if (access != null && refresh == null) {
            throw new TokensUpdationException("You can't renew an access token without a refresh token");
        }

        this.accessToken = access;
        this.refreshToken = refresh;
    }

    public synchronized void setUser(User user) {
        this.user = user;

        this.state = (user != null) ? SessionState.AUTHORIZED : SessionState.NOT_AUTHORIZED;
    }

    public synchronized boolean isAuthorized() {
        return state == SessionState.AUTHORIZED;
    }

    public synchronized String getAccessToken() {
        return accessToken;
    }

    public synchronized String getRefreshToken() {
        return refreshToken;
    }

    public synchronized User getUser() {
        return user;
    }
}
