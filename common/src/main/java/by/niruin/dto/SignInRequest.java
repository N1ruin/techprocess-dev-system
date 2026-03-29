package by.niruin.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record SignInRequest(
        @NotNull(message = "Поле не должно быть пустым!")
        @Length(min = 5, max = 16, message = "Логин должен быть длиной от {min} до {max} символов!")
        @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Имя пользователя может содержать только латинские буквы и цифры!")
        String login,
        String password) {
}
