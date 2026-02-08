package by.niruin.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthenticationResponse {
    private String accessToken;
    private String refreshToken;
}
