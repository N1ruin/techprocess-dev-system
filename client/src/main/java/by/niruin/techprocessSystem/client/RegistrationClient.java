package by.niruin.techprocessSystem.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class RegistrationClient {
    private final RestClient restClient;

    public SignUpResponse signUp(RegistrationRequest request) {
        return restClient.post()
                .uri("/api/auth/signup")
                .body(request)
                .retrieve()
                .body(SignUpResponse.class);
    }
}
