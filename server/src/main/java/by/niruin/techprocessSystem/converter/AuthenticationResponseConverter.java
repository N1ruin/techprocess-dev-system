package by.niruin.techprocessSystem.converter;

import by.niruin.dto.AuthenticationResponse;
import by.niruin.techprocessSystem.domain.model.AuthenticationTokens;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationResponseConverter implements Converter<AuthenticationTokens, AuthenticationResponse> {
    @Override
    public AuthenticationResponse convert(AuthenticationTokens tokens) {
        return new AuthenticationResponse(tokens.access(), tokens.refresh());
    }
}
