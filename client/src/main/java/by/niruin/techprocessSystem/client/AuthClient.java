package by.niruin.techprocessSystem.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class AuthClient {
    private final RestClient restClient;

    public AuthenticationResponse signIn(AuthenticationRequest request) {
        return restClient.post()
                .uri("/api/v1/auth/signin")
                .body(request)
                .retrieve()
                .body(AuthenticationResponse.class);
    }

    public void logout(UserLogoutRequest request) {
        restClient.post()
                .uri("/api/v1/auth/logout")
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }
}
