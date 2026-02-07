package by.niruin.techprocessSystem.domain.service;

import by.niruin.techprocessSystem.domain.dto.AuthenticationRequest;
import by.niruin.techprocessSystem.domain.dto.RegistrationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.web.client.RestClient;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class AuthenticationServiceTest {
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private RestClient restClient;
    @InjectMocks
    private AuthenticationService authenticationService;

    private AuthenticationRequest request;

    @BeforeEach
    void setUp() {
        request = new AuthenticationRequest();
        request.setLogin("testLogin12");
        request.setPassword("testPass123^");
    }

    @Test
    void signInSuccess() throws Exception {
        when(restClient.post()
                .uri(anyString())
                .body(any(AuthenticationRequest.class))
                .retrieve()
                .body(String.class))
                .thenReturn("User authenticated!");

        var result = authenticationService.signIn(request);

        assertNotNull(result);
        assertEquals("User authenticated!", result.get());
    }

    @Test
    void signUpServerError() {
        when(restClient.post()
                .uri(anyString())
                .body(any(RegistrationRequest.class))
                .retrieve()
                .body(String.class))
                .thenThrow(new RuntimeException("Connection failed"));

        var result = authenticationService.signIn(request);

        assertTrue(result.isCompletedExceptionally());
        ExecutionException executionException = assertThrows(ExecutionException.class, result::get);
        assertTrue(executionException.getCause().getMessage().contains("Connection failed"));
    }

}