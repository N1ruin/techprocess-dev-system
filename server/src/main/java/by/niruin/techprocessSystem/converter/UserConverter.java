package by.niruin.techprocessSystem.converter;

import by.niruin.dto.SignUpRequest;
import by.niruin.techprocessSystem.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserConverter implements Converter<SignUpRequest, User> {
    private final PasswordEncoder passwordEncoder;

    @Override
    public User convert(SignUpRequest request) {
        var user = new User();
        user.setUsername(request.login());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setFatherName(request.fatherName());
        user.setBirthDate(request.birthDate());

        return user;
    }
}
