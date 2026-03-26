package by.niruin.techprocessSystem.domain.controller;

import by.niruin.dto.SignInRequest;
import by.niruin.dto.AuthenticationResponse;
import by.niruin.dto.SignUpRequest;
import by.niruin.techprocessSystem.converter.UserConverter;
import by.niruin.techprocessSystem.domain.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserConverter userConverter;

    @PostMapping("/signup")
    public ResponseEntity<AuthenticationResponse> signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
        var user = userConverter.convert(signUpRequest);

        var response = authenticationService.signUp(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthenticationResponse> signIn(@RequestBody @Valid SignInRequest signInRequest) {
        var response = authenticationService.signIn(signInRequest);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refresh(@AuthenticationPrincipal Jwt jwt) {
        var token = jwt.getTokenValue();

        var response = authenticationService.refreshToken(token);

        return ResponseEntity.ok(response);
    }
}
