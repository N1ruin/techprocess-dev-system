package by.niruin.techprocessSystem.domain.service;

import by.niruin.dto.AuthenticationRequest;
import by.niruin.dto.AuthenticationResponse;
import by.niruin.techprocessSystem.domain.entity.ApplicationSession;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final RestClient restClient;
    private final ApplicationSession applicationSession;

    @Async
    public CompletableFuture<AuthenticationResponse> signIn(AuthenticationRequest request) {
        try {
            var response = restClient.post()
                    .uri("/api/auth/signin")
                    .body(request)
                    .retrieve()
                    .body(AuthenticationResponse.class);

            if (response != null) {
                applicationSession.setAccessToken(response.getAccessToken());
                applicationSession.setRefreshToken(response.getRefreshToken());
            }
            return CompletableFuture.completedFuture(response);
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    public void logout() {
        applicationSession.setAccessToken(null);
        applicationSession.setRefreshToken(null);
    }
}
