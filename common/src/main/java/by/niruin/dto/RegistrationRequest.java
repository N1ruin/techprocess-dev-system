package by.niruin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrationRequest {
    @NotBlank(message = "Поле не должно быть пустым!")
    @Length(min = 5, max = 16, message = "Логин должен быть длиной от {min} до {max} символов!")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Имя пользователя может содержать только латинские буквы и цифры!")
    private String login;

    @NotBlank(message = "Поле не должно быть пустым!")
    @Length(min = 5, max = 16, message = "Длина пароля должна быть от {min} до {max} символов!")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)[a-zA-Z\\d@#$%^&+=]*$",
            message = "Пароль может содержать только латинские буквы, цифры и символы @#$%^&+=!")
    private String password;

    @NotBlank(message = "Поле не должно быть пустым!")
    @Pattern(regexp = "[а-яА-Я]*", message = "Имя может содержать только русские буквы и цифры!")
    private String firstName;

    @NotBlank(message = "Поле не должно быть пустым!")
    @Pattern(regexp = "[а-яА-Я]*", message = "Фамилия может содержать только русские буквы!")
    private String lastName;

    @NotBlank(message = "Поле не должно быть пустым!")
    @Pattern(regexp = "[а-яА-Я]*", message = "Отчество может содержать только русские буквы!")
    private String surname;

    @NotNull(message = "Поле не должно быть пустым!")
    @Past(message = "Дата рождения должна быть в прошлом!")
    private LocalDate birthDate;
}
