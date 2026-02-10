package by.niruin.techprocessSystem.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationSession {
    private String accessToken;
    private String refreshToken;
    private User user;
}
