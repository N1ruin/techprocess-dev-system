package by.niruin.techprocessSystem.domain.service;

import by.niruin.techprocessSystem.domain.dto.RegistrationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.concurrent.CompletableFuture;

@Service
public class RegistrationService {
    @Autowired
    private RestClient restClient;

    @Async
    public CompletableFuture<String> signUp(RegistrationRequest request) {
        try {
            var response = restClient.post()
                    .uri("/api/auth/signup")
                    .body(request)
                    .retrieve()
                    .body(String.class);
            return CompletableFuture.completedFuture(response);
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }
}
