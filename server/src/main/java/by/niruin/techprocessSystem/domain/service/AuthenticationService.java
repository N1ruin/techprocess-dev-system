package by.niruin.techprocessSystem.domain.service;

import by.niruin.dto.AuthenticationRequest;
import by.niruin.dto.AuthenticationResponse;
import by.niruin.techprocessSystem.domain.entity.Role;
import by.niruin.techprocessSystem.domain.entity.User;
import by.niruin.techprocessSystem.domain.repository.UserRepository;
import by.niruin.techprocessSystem.sequrity.jwt.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse signUp(by.niruin.dto.RegistrationRequest registrationRequest) throws AuthenticationException {
        var user = User.builder()
                .username(registrationRequest.getLogin())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .role(Role.ROLE_ENGINEER)
                .isActive(true)
                .registrationDate(LocalDateTime.now())
                .lastWorkingDate(LocalDateTime.now())
                .firstName(registrationRequest.getFirstName())
                .lastName(registrationRequest.getLastName())
                .surname(registrationRequest.getSurname())
                .birthDate(registrationRequest.getBirthDate())
                .build();

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new AuthenticationException("Пользователь с таким именем уже существует!");
        }

        userRepository.save(user);

        return getAuthenticationResponse(user);
    }

    public AuthenticationResponse signIn(AuthenticationRequest authRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getLogin(), authRequest.getPassword()));

        var user = userRepository.findByUsername(authRequest.getLogin())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return getAuthenticationResponse(user);
    }

    public AuthenticationResponse refreshToken(String refreshToken) {
        try {
            String username = jwtService.extractUsername(refreshToken);

            var user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            if (jwtService.isTokenValid(refreshToken, user)) {
                return getAuthenticationResponse(user);
            }
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Refresh token expired. Please log in again.");
        }

        throw new RuntimeException("Invalid Refresh Token");
    }

    private AuthenticationResponse getAuthenticationResponse(User user) {
        var authResponse = new AuthenticationResponse();
        authResponse.setAccessToken(jwtService.generateAccessToken(user));
        authResponse.setRefreshToken(jwtService.generateRefreshToken(user));
        return authResponse;
    }
}
