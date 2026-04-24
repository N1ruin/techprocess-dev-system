package by.niruin.techprocessSystem.domain.service;

import by.niruin.dto.*;
import by.niruin.techprocessSystem.client.AuthClient;
import by.niruin.techprocessSystem.domain.entity.ApplicationSession;
import by.niruin.techprocessSystem.exception.AuthenticationException;
import by.niruin.techprocessSystem.exception.LogoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthClient authClient;
    private final ApplicationSession applicationSession;

    @Async
    public CompletableFuture<AuthenticationResponse> signIn(AuthenticationRequest request) {
        try {
            var response = authClient.signIn(request);

            if (response != null) {
                applicationSession.updateTokens(response.accessToken(), response.refreshToken());
            }
            return CompletableFuture.completedFuture(response);
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    @Async
    public CompletableFuture<Void> logout(UserLogoutRequest request) {
        try {
            authClient.logout(request);

            applicationSession.clear();

            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            log.error("Failed to logout for user {}", request.username());
            return CompletableFuture.failedFuture(new LogoutException("Failed to logout for user %s"
                    .formatted(request.username())));
        }
    }

    public void refreshTokens() {
        AuthenticationResponse authResponse;
        try {
            authResponse = authClient.refresh(applicationSession.getRefreshToken());
        } catch (Exception e) {
            throw new AuthenticationException("Token expired. Please login again");
        }

        if (authResponse == null) {
            throw new AuthenticationException("Empty response when refreshing tokens.");
        }

        applicationSession.updateTokens(authResponse.accessToken(), authResponse.refreshToken());
    }
}
