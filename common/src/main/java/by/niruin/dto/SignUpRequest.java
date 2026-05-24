package by.niruin.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

public record SignUpRequest(
        @NotNull(message = "Поле не должно быть пустым!")
        @Length(min = 5, max = 16, message = "Логин должен быть длиной от {min} до {max} символов!")
        @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Имя пользователя может содержать только латинские буквы и цифры!")
        String login,

        @NotNull(message = "Поле не должно быть пустым!")
        @Length(min = 5, max = 16, message = "Длина пароля должна быть от {min} до {max} символов!")
        String password,

        @NotNull(message = "Поле не должно быть пустым!")
        @Pattern(regexp = "[а-яА-Я]*", message = "Имя может содержать только русские буквы и цифры!")
        String firstName,

        @NotNull(message = "Поле не должно быть пустым!")
        @Pattern(regexp = "[а-яА-Я]*", message = "Фамилия может содержать только русские буквы!")
        String lastName,

        @NotNull(message = "Поле не должно быть пустым!")
        @Pattern(regexp = "[а-яА-Я]*", message = "Отчество может содержать только русские буквы!")
        String fatherName,

        @NotNull(message = "Поле не должно быть пустым!")
        @Past(message = "Дата рождения должна быть в прошлом!")
        LocalDate birthDate) {
}
