package by.niruin.dto.equipment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetEquipmentsResponse {
    private String index;
    private String note;
    private String imagePath;
}