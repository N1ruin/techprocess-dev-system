package by.niruin.techprocessSystem.domain.service;

import by.niruin.dto.RegistrationRequest;
import by.niruin.dto.SignUpResponse;
import by.niruin.techprocessSystem.client.RegistrationClient;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final RegistrationClient registrationClient;

    @Async
    public CompletableFuture<SignUpResponse> signUp(RegistrationRequest request) {
        try {
            var response = registrationClient.signUp(request);

            return CompletableFuture.completedFuture(response);
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }
}
