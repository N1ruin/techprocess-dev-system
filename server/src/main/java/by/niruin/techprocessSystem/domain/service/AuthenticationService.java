package by.niruin.techprocessSystem.domain.service;

import by.niruin.dto.SignInRequest;
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
    public AuthenticationResponse signUp(User user) {
        var login = user.getUsername();

        if (userRepository.findByUsername(login).isPresent()) {
            throw new AuthenticationException("User with login %s exist!".formatted(login));
        }
        userRepository.save(user);

        return getAuthenticationResponse(user);
    }

    @Transactional
    public AuthenticationResponse signIn(SignInRequest signInRequest) {
        var authToken = new UsernamePasswordAuthenticationToken(signInRequest.getLogin(), signInRequest.getPassword());
        authenticationManager.authenticate(authToken);

        var user = userRepository.findByUsername(signInRequest.getLogin())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return getAuthenticationResponse(user);
    }

    @Transactional
    public AuthenticationResponse refreshToken(String refreshToken) {
        try {
            String username = jwtService.extractUsername(refreshToken);

            var user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            if (user.getRefreshToken() == null || !user.getRefreshToken().equals(refreshToken)) {
                throw new InvalidTokenException("Invalid or revoked refresh token");
            }

            return getAuthenticationResponse(user);
        } catch (JwtException e) {
            throw new TokenExpiredException("Refresh token expired. Please log in again.");
        }
    }

    @Transactional
    private AuthenticationResponse getAuthenticationResponse(User user) {
        var tokensResponse = jwtService.generateAccessAndRefreshTokens(user);

        user.setRefreshToken(tokensResponse.getRefreshToken());
        user.setLastWorkingDate(LocalDateTime.now());
        userRepository.save(user);

        return tokensResponse;
    }
}
