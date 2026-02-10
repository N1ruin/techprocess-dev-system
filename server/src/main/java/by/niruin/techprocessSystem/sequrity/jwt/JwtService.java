package by.niruin.techprocessSystem.sequrity.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {
    @Value("774880c22fd0b4f08ac80b4569e788802ed4077100144959dbcbdb3f48c82915")
    private String jwtSecret;

    public String generateAccessToken(UserDetails userDetails) {
        return generateToken(userDetails, 1000 * 60 * 15);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return generateToken(userDetails, 1000 * 60 * 60 * 24 * 7);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private String generateToken(UserDetails userDetails, long expirationTime) {
        Date current = new Date(System.currentTimeMillis());
        Date expiration = new Date(System.currentTimeMillis() + expirationTime);
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(current)
                .expiration(expiration)
                .signWith(getSecretKey())
                .compact();
    }

    private boolean isTokenExpired(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .before(new Date());
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
