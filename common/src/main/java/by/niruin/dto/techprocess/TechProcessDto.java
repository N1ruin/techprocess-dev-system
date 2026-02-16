package by.niruin.dto.techprocess;

import by.niruin.entity.TechprocessType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TechProcessDto {
    private String inventoryNumber;
    private String partNumber;
    private String name;
    private TechprocessType type;
    private LocalDate creationDate;
    private String foundation;//перевести нормально)
    private String authorLastName;
    private String designAssignmentNumber;
}

