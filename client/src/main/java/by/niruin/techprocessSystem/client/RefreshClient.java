package by.niruin.techprocessSystem.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class RefreshClient {
    private final RestClient refreshRestClient;

    public AuthenticationResponse refresh(String refreshToken) {
        return refreshRestClient.post()
                .uri("/api/v1/auth/refresh")
                .body(new RefreshRequest(refreshToken))
                .retrieve()
                .body(AuthenticationResponse.class);
    }
}
