package by.niruin.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record SignUpResponse(Long id,
                             String username,
                             boolean isActive,
                             String firstName,
                             String lastName,
                             String fatherName,
                             LocalDate birthDate,
                             LocalDateTime registrationDate,
                             LocalDateTime lastWorkingDate,
                             String role) {
}
