package by.niruin.techprocessSystem.domain.service;

import by.niruin.dto.AuthenticationResponse;
import by.niruin.dto.RegistrationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final RestClient restClient;

    @Async
    public CompletableFuture<AuthenticationResponse> signUp(RegistrationRequest request) {
        try {
            var response = restClient.post()
                    .uri("/api/auth/signup")
                    .body(request)
                    .retrieve()
                    .body(AuthenticationResponse.class);
            return CompletableFuture.completedFuture(response);
        } catch (HttpClientErrorException e) {
            return CompletableFuture.failedFuture(e);
        }
    }
}
