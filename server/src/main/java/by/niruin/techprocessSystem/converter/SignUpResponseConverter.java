package by.niruin.techprocessSystem.converter;

import by.niruin.dto.SignUpResponse;
import by.niruin.techprocessSystem.domain.entity.User;
import org.springframework.core.convert.converter.Converter;

public class SignUpResponseConverter implements Converter<User, SignUpResponse> {
    @Override
    public SignUpResponse convert(User user) {
        var id = user.getId();
        var userName = user.getUsername();
        var isActive = user.isActive();
        var registrationDate = user.getRegistrationDate();
        var lastWorkingDate = user.getLastWorkingDate();
        var firstName = user.getFirstName();
        var lastName = user.getLastName();
        var fatherName = user.getFatherName();
        var birthDate = user.getBirthDate();
        var role = user.getRole().name();

        return new SignUpResponse(id, userName, isActive, firstName, lastName, fatherName, birthDate, registrationDate,
                lastWorkingDate, role);
    }
}
