package by.niruin.techprocessSystem.config;

import by.niruin.techprocessSystem.domain.entity.ApplicationSession;
import by.niruin.techprocessSystem.domain.service.AuthenticationService;
import by.niruin.techprocessSystem.exception.AuthenticationException;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

@Component
public class JwtRefreshInterceptor implements ClientHttpRequestInterceptor {
    private final ApplicationSession session;
    private final AuthenticationService authenticationService;

    public JwtRefreshInterceptor(ApplicationSession session, @Lazy AuthenticationService authenticationService) {
        this.session = session;
        this.authenticationService = authenticationService;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        String path = request.getURI().getPath();

        if (path.contains("/api/v1/auth/signin") || path.contains("/api/v1/auth/refresh")) {
            return execution.execute(request, body);
        }

        String token = session.getAccessToken();
        if (token != null && !token.isEmpty()) {
            request.getHeaders().setBearerAuth(token);
        }

        var response = execution.execute(request, body);

        if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            response.close();

            synchronized (this) {
                if (Objects.equals(token, session.getAccessToken())) {
                    try {
                        authenticationService.refreshTokens(request);
                    } catch (AuthenticationException e) {
                        throw new IOException("Refresh failed", e);
                    }
                }
            }

            request.getHeaders().setBearerAuth(session.getAccessToken());

            return execution.execute(request, body);
        }

        return response;
    }
}
