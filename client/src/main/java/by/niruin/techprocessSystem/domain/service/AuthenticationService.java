package by.niruin.techprocessSystem.domain.service;

import by.niruin.dto.AuthenticationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.concurrent.CompletableFuture;

@Service
public class AuthenticationService {
    @Autowired
    private RestClient restClient;

    @Async
    public CompletableFuture<String> signIn(AuthenticationRequest request) {
        try {
            var response = restClient.post()
                    .uri("/auth/signin")
                    .body(request)
                    .retrieve()
                    .body(String.class);
            return CompletableFuture.completedFuture(response);
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }
}
