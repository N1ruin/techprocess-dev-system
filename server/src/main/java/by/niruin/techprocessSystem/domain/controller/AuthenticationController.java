package by.niruin.techprocessSystem.domain.controller;

import by.niruin.dto.SignInRequest;
import by.niruin.dto.AuthenticationResponse;
import by.niruin.dto.SignUpRequest;
import by.niruin.dto.SignUpResponse;
import by.niruin.techprocessSystem.converter.SignUpResponseConverter;
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
    private final SignUpResponseConverter signUpResponseConverter;

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponse> signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
        var user = userConverter.convert(signUpRequest);

        var createdUser = authenticationService.signUp(user);

        var response = signUpResponseConverter.convert(createdUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthenticationResponse> signIn(@RequestBody @Valid SignInRequest signInRequest) {
        var login = signInRequest.login();
        var password = signInRequest.password();

        var response = authenticationService.signIn(login, password);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refresh(@AuthenticationPrincipal Jwt jwt) {
        var token = jwt.getTokenValue();

        var response = authenticationService.refreshToken(token);

        return ResponseEntity.ok(response);
    }
}
