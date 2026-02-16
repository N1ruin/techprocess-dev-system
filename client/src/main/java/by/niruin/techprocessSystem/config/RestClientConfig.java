package by.niruin.techprocessSystem.config;

import by.niruin.dto.auth.AuthenticationResponse;
import by.niruin.dto.auth.RefreshRequest;
import by.niruin.techprocessSystem.domain.entity.ApplicationSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import javax.security.sasl.AuthenticationException;
import java.util.Objects;

@Configuration
@RequiredArgsConstructor
public class RestClientConfig {
    @Value("${web.server-url}")
    private String serverUrl;
    private final ApplicationSession session;

    @Bean
    public RestClient authRestClient() {
        return RestClient.builder()
                .baseUrl(serverUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean
    public RestClient restClient(RestClient authRestClient) {
        return RestClient.builder()
                .baseUrl(serverUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .requestInterceptor((request, body, execution) -> {
                    var token = session.getAccessToken();
                    if (token != null && !token.isEmpty()) {
                        request.getHeaders().setBearerAuth(token);
                    }

                    var response = execution.execute(request, body);

                    if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                        response.close();

                        refreshTokens(authRestClient, request, token);

                        return execution.execute(request, body);
                    }

                    return response;
                })
                .build();
    }

    private synchronized void refreshTokens(RestClient authRestClient, HttpRequest request, String token) throws AuthenticationException {
        var currentToken = session.getAccessToken();
        if (Objects.equals(token, currentToken)) {
            try {
                var authResponse = getAuthenticationResponse(authRestClient);

                if (authResponse != null) {
                    session.setAccessToken(authResponse.getAccessToken());
                    session.setRefreshToken(authResponse.getRefreshToken());
                }
            } catch (Exception e) {
                throw new AuthenticationException("Token expired. Please login again");
            }
        }
        request.getHeaders().setBearerAuth(session.getAccessToken());
    }

    private AuthenticationResponse getAuthenticationResponse(RestClient authRestClient) {
        return authRestClient.post()
                .uri("/api/auth/refresh")
                .body(new RefreshRequest(session.getRefreshToken()))
                .retrieve()
                .body(AuthenticationResponse.class);
    }
}
