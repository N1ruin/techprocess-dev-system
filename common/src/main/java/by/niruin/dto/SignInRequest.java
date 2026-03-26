package by.niruin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
public class SignInRequest {
    @NotBlank(message = "Поле не должно быть пустым!")
    @Length(min = 5, max = 16, message = "Логин должен быть длиной от {min} до {max} символов!")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Имя пользователя может содержать только латинские буквы и цифры!")
    private String login;

    private String password;
}