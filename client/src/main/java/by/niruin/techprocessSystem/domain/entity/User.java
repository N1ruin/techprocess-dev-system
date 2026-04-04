package by.niruin.techprocessSystem.domain.entity;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class User {
    private long id;
    private String username;
    private String firstName;
    private String lastName;
    private String surname;
    private LocalDate birthDate;
}
