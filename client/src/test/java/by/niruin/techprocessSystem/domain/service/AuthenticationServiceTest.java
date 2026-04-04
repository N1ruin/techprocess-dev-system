package by.niruin.techprocessSystem.domain.service;

import by.niruin.dto.AuthenticationRequest;

import by.niruin.dto.UserLogoutRequest;
import by.niruin.techprocessSystem.config.AsyncConfig;
import by.niruin.techprocessSystem.config.JwtRefreshInterceptor;
import by.niruin.techprocessSystem.config.RestClientConfig;
import by.niruin.techprocessSystem.domain.entity.ApplicationSession;
import by.niruin.techprocessSystem.domain.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.wiremock.spring.EnableWireMock;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureWebClient
@SpringBootTest(classes = {RestClientConfig.class, AsyncConfig.class, AuthenticationService.class,
        ApplicationSession.class, JwtRefreshInterceptor.class},
        properties = "web.server-url=http://localhost:${wiremock.server.port}")
@EnableWireMock
class AuthenticationServiceTest {
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private ApplicationSession applicationSession;
    @Autowired
    private JwtRefreshInterceptor jwtRefreshInterceptor;

    @BeforeEach
    void clear() {
        applicationSession.clear();
    }

    @Test
    void testSignInSuccess() throws Exception {
        stubFor(post(urlEqualTo("/api/v1/auth/signin"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                    {
                                        "accessToken": "test-access",
                                        "refreshToken": "test-refresh"
                                    }
                                """)));
        var request = new AuthenticationRequest("user", "password");
        var future = authenticationService.signIn(request);

        var response = future.get(5, TimeUnit.SECONDS);

        assertNotNull(response);
        assertEquals("test-access", response.accessToken());
        assertEquals("test-refresh", response.refreshToken());
        assertEquals("test-access", applicationSession.getAccessToken());
        assertEquals("test-refresh", applicationSession.getRefreshToken());

        verify(postRequestedFor(urlEqualTo("/api/v1/auth/signin"))
                .withRequestBody(containing("user")));
    }

    @Test
    void shouldHandleLogoutSuccess() throws Exception {
        stubFor(post(urlPathEqualTo("/api/v1/auth/logout"))
                .willReturn(ok()));

        applicationSession.setAccessToken("test-access");
        applicationSession.setRefreshToken("test-refresh");
        applicationSession.setUser(new User(1, "testUserName", "testFirstName", "testLastName",
                "testFatherName", LocalDate.now()));

        var logoutRequest = new UserLogoutRequest("testUserName");

        var logoutFuture = authenticationService.logout(logoutRequest);
        logoutFuture.get(5, TimeUnit.SECONDS);

        assertNull(applicationSession.getAccessToken());
        assertNull(applicationSession.getRefreshToken());
        assertNull(applicationSession.getUser());
    }
}
