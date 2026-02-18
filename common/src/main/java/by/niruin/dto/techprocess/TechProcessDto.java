package by.niruin.dto.techprocess;

import by.niruin.entity.TechprocessStatus;
import by.niruin.entity.TechprocessType;
import by.niruin.entity.WorkType;
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
    private String partName;
    private String base;
    private TechprocessType type;
    private WorkType workType;
    private String authorLastName;
    private LocalDate creationDate;
    private TechprocessStatus status;
}

