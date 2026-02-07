package by.niruin.techprocessSystem.domain.service;

import by.niruin.techprocessSystem.domain.dto.RegistrationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.concurrent.ExecutionException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class RegistrationServiceTest {
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private RestClient restClient;
    @InjectMocks
    private RegistrationService registrationService;

    private RegistrationRequest request;

    @BeforeEach
    void setUp() {
        request = RegistrationRequest.builder()
                .login("testUser")
                .password("qwerty123%")
                .firstName("Иван")
                .lastName("Иванов")
                .surname("Иванович")
                .birthDate(LocalDate.now().minusYears(50L))
                .build();
    }

    @Test
    void signUpSuccess() throws Exception {
        when(restClient.post()
                .uri(anyString())
                .body(any(RegistrationRequest.class))
                .retrieve()
                .body(String.class))
                .thenReturn("User registered");

        var result = registrationService.signUp(request);

        assertNotNull(result);
        assertEquals("User registered", result.get());
    }

    @Test
    void signUpServerError() {
        when(restClient.post()
                .uri(anyString())
                .body(any(RegistrationRequest.class))
                .retrieve()
                .body(String.class))
                .thenThrow(new RuntimeException("Connection failed"));

        var result = registrationService.signUp(request);

        assertTrue(result.isCompletedExceptionally());
        ExecutionException executionException = assertThrows(ExecutionException.class, result::get);
        assertTrue(executionException.getCause().getMessage().contains("Connection failed"));
    }
}