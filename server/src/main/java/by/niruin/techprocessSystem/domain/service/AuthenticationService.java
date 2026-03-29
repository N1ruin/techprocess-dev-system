package by.niruin.techprocessSystem.domain.service;

import by.niruin.dto.AuthenticationResponse;
import by.niruin.techprocessSystem.domain.entity.User;
import by.niruin.techprocessSystem.domain.repository.UserRepository;
import by.niruin.techprocessSystem.exception.AuthenticationException;
import by.niruin.techprocessSystem.exception.InvalidTokenException;
import by.niruin.techprocessSystem.exception.TokenExpiredException;
import by.niruin.techprocessSystem.exception.UsernameNotFoundException;
import by.niruin.techprocessSystem.sequrity.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public User signUp(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new AuthenticationException("User with login %s exist!".formatted(user.getUsername()));
        }

        return userRepository.save(user);
    }

    @Transactional
    public AuthenticationResponse signIn(String login, String password) {
        var authToken = new UsernamePasswordAuthenticationToken(login, password);
        authenticationManager.authenticate(authToken);

        var user = userRepository.findByUsername(login)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return getAuthenticationResponse(user);
    }

    @Transactional
    public AuthenticationResponse refreshToken(String refreshToken) {
        String username;
        try {
            username = jwtService.extractUsername(refreshToken);
        } catch (JwtException e) {
            throw new TokenExpiredException("Refresh token expired. Please log in again.");
        }

        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (user.getRefreshToken() == null || !user.getRefreshToken().equals(refreshToken)) {
            throw new InvalidTokenException("Invalid or revoked refresh token");
        }

        return getAuthenticationResponse(user);
    }

    @Transactional
    private AuthenticationResponse getAuthenticationResponse(User user) {
        var tokensResponse = jwtService.generateAccessAndRefreshTokens(user);

        user.setRefreshToken(tokensResponse.refreshToken());
        user.setLastWorkingDate(LocalDateTime.now());
        userRepository.save(user);

        return tokensResponse;
    }
}
