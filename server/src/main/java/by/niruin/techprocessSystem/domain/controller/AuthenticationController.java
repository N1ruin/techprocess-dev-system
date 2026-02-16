package by.niruin.techprocessSystem.domain.controller;

import by.niruin.dto.auth.AuthenticationRequest;
import by.niruin.dto.auth.AuthenticationResponse;
import by.niruin.dto.auth.RefreshRequest;
import by.niruin.dto.auth.RegistrationRequest;
import by.niruin.techprocessSystem.domain.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<AuthenticationResponse> signUp(@RequestBody RegistrationRequest registrationRequest) throws AuthenticationException {
        var response = authenticationService.signUp(registrationRequest);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthenticationResponse> signIn(@RequestBody AuthenticationRequest authenticationRequest) {
        var response = authenticationService.signIn(authenticationRequest);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refresh(@RequestBody RefreshRequest request) {
        var response = authenticationService.refreshToken(request.getRefreshToken());

        return ResponseEntity.ok(response);
    }
}
