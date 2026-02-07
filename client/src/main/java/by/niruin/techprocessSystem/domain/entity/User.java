package by.niruin.techprocessSystem.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private long id;
    private String username;
    private String firstName;
    private String lastName;
    private String surname;
    private LocalDate birthDate;
}
