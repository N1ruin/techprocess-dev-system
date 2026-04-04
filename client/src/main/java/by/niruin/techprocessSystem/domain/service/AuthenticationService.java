package by.niruin.techprocessSystem.domain.service;

import by.niruin.dto.AuthenticationRequest;
import by.niruin.dto.AuthenticationResponse;
import by.niruin.dto.UserLogoutRequest;
import by.niruin.techprocessSystem.domain.entity.ApplicationSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import javax.security.sasl.AuthenticationException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService {
    private final RestClient restClient;
    private final ApplicationSession applicationSession;

    @Async
    public CompletableFuture<AuthenticationResponse> signIn(AuthenticationRequest request) {
        try {
            var response = restClient.post()
                    .uri("/api/v1/auth/signin")
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

    @Async
    public CompletableFuture<Void> logout(UserLogoutRequest request) {
        try {
            restClient.post()
                    .uri("/api/v1/auth/logout")
                    .body(request)
                    .retrieve()
                    .toBodilessEntity();

            applicationSession.clear();

            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            log.error("Failed to logout for user {}", request.username());
            return CompletableFuture.failedFuture(new LogoutException("Failed to logout for user"
                    .formatted(request.username())));
        }
    }

    public void refreshTokens(HttpRequest request, String token) {
        AuthenticationResponse authResponse;

        try {
            authResponse = restClient.post()
                    .uri("/api/v1/auth/refresh")
                    .body(new RefreshRequest(applicationSession.getRefreshToken()))
                    .retrieve()
                    .body(AuthenticationResponse.class);
        } catch (Exception e) {
            throw new AuthException("Token expired. Please login again");
        }

        if (authResponse == null) {
            throw new AuthException("Empty response when refreshing tokens.");
        }

        applicationSession.setAccessToken(authResponse.getAccessToken());
        applicationSession.setRefreshToken(authResponse.getRefreshToken());

        request.getHeaders().setBearerAuth(applicationSession.getAccessToken());
    }
}
