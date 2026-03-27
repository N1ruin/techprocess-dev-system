package by.niruin.techprocessSystem.converter;

import by.niruin.dto.SignUpRequest;
import by.niruin.techprocessSystem.domain.entity.Role;
import by.niruin.techprocessSystem.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class UserConverter implements Converter<SignUpRequest, User> {
    private final PasswordEncoder passwordEncoder;

    @Override
    public User convert(SignUpRequest request) {
        var user = new User();
        user.setUsername(request.getLogin());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.ROLE_ENGINEER);
        user.setActive(true);
        user.setRegistrationDate(LocalDateTime.now());
        user.setLastWorkingDate(LocalDateTime.now());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setFatherName(request.getFathername());
        user.setBirthDate(request.getBirthDate());

        return user;
    }
}
