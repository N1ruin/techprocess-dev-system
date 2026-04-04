package by.niruin.techprocessSystem.domain.entity;

import by.niruin.techprocessSystem.domain.controller.Cleanable;
import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class ApplicationSession implements Cleanable {
    private String accessToken;
    private String refreshToken;
    private User user;

    @Override
    public void clear() {
        this.accessToken = null;
        this.refreshToken = null;
        this.user = null;
    }
}
