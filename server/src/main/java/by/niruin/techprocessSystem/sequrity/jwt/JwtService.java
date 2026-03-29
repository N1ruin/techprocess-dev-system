package by.niruin.techprocessSystem.sequrity.jwt;

import by.niruin.dto.AuthenticationResponse;
import by.niruin.techprocessSystem.config.JwtProperties;
import by.niruin.techprocessSystem.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final JwtProperties jwtProperties;
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    public AuthenticationResponse generateAccessAndRefreshTokens(User user) {
        var access = generateToken(user, jwtProperties.getAccessExpiredTime());
        var refresh = generateToken(user, jwtProperties.getRefreshExpiredTime());

        return new AuthenticationResponse(access, refresh);
    }

    private String generateToken(User user, long expirationTime) {
        var claimsSet = JwtClaimsSet.builder()
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusMillis(expirationTime))
                .subject(user.getUsername())
                .claim("roles", user.getRole().name())
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }

    public String extractUsername(String token) {
        return jwtDecoder.decode(token).getSubject();
    }
}
