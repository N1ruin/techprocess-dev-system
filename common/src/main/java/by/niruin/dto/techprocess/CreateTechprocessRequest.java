package by.niruin.dto.techprocess;

import by.niruin.entity.TechprocessType;
import by.niruin.entity.WorkType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTechprocessRequest {
    private String inventoryNumber;
    private TechprocessType type;
    private String techprocessName;
    private String partNumber;
    private WorkType workType;
}
