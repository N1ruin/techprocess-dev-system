package by.niruin.techprocessSystem.domain.entity;

import by.niruin.techprocessSystem.domain.controller.Cleanable;
import org.springframework.stereotype.Component;

@Component
public class ApplicationSession implements Cleanable {
    private String accessToken;
    private String refreshToken;
    private User user;

    @Override
    public synchronized void clear() {
        this.accessToken = null;
        this.refreshToken = null;
        this.user = null;
    }

    public synchronized String getAccessToken() {
        return accessToken;
    }

    public synchronized void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public synchronized String getRefreshToken() {
        return refreshToken;
    }

    public synchronized void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public synchronized User getUser() {
        return user;
    }

    public synchronized void setUser(User user) {
        this.user = user;
    }
}
