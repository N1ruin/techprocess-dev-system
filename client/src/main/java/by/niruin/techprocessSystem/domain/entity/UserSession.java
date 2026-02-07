package by.niruin.techprocessSystem.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSession {
    private String token;
    private User user;

    public boolean isLogging() {
        return !token.isEmpty();
    }
}
