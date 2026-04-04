package by.niruin.techprocessSystem.domain.service;

import by.niruin.dto.RegistrationRequest;
import by.niruin.dto.SignUpResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.concurrent.ExecutionException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {
    @Mock
    private RestClient restClient;
    @InjectMocks
    private RegistrationService registrationService;
    private RegistrationRequest request;

    @BeforeEach
    void setUp() {
        var login = "testUser";
        var password = "qwerty123%";
        var firstName = "Иван";
        var lastName = "Иванов";
        var fatherName = "Иванович";
        var birthDate = LocalDate.now().minusYears(50L);

        request = new RegistrationRequest(login, password, firstName, lastName, fatherName, birthDate);
    }

    @Test
    void signUpSuccess() throws Exception {
        var id = 1L;
        var requestBodyUriSpec = mock(RestClient.RequestBodyUriSpec.class);
        var requestBodySpec = mock(RestClient.RequestBodySpec.class);
        var responseSpec = mock(RestClient.ResponseSpec.class);
        var expectedResponse = new SignUpResponse(id, request.login(),
                request.firstName(), request.lastName(), request.fatherName(), request.birthDate());

        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any(RegistrationRequest.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(SignUpResponse.class)).thenReturn(expectedResponse);

        var result = registrationService.signUp(request);

        assertNotNull(result);
        assertEquals(expectedResponse, result.get());

    }

    @Test
    void signUpServerError() {
        var requestBodyUriSpec = mock(RestClient.RequestBodyUriSpec.class);
        var requestBodySpec = mock(RestClient.RequestBodySpec.class);

        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any(RegistrationRequest.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenThrow(new RuntimeException("Connection failed"));

        var result = registrationService.signUp(request);

        assertTrue(result.isCompletedExceptionally());
        ExecutionException executionException = assertThrows(ExecutionException.class, result::get);
        assertTrue(executionException.getCause().getMessage().contains("Connection failed"));
    }
}
