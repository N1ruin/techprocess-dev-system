package by.niruin.techprocessSystem.domain.service;

import by.niruin.dto.equipment.GetTenEquipmentsResponse;
import by.niruin.dto.equipment.TechnologicalEquipmentDto;
import by.niruin.techprocessSystem.domain.entity.TechnologicalEquipment;
import by.niruin.techprocessSystem.domain.repository.TechnologicalEquipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TechnologicalEquipmentService {
    private final TechnologicalEquipmentRepository technologicalEquipmentRepository;
    @Value("${upload.path}")
    private String uploadPath;

    public void add(TechnologicalEquipmentDto dto, MultipartFile file) {
        try {
            var fileName = UUID.randomUUID() + ".png";

            File dir = new File(uploadPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            var filePath = uploadPath + fileName;
            System.out.println(filePath);
            Files.write(Paths.get(filePath), file.getBytes());

            TechnologicalEquipment entity = TechnologicalEquipment.builder()
                    .equipmentIndex(dto.getIndex())
                    .note(dto.getNote())
                    .imagePath(filePath)
                    .build();

            technologicalEquipmentRepository.save(entity);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при сохранении изображения:", e);
        }
    }

    public List<GetTenEquipmentsResponse> getLastTenEquipment() {
        var equipments = technologicalEquipmentRepository.findTop10ByOrderByIdDesc();

        return equipments.stream()
                .map(equipment -> {
                    try {
                        return new GetTenEquipmentsResponse(equipment.getEquipmentIndex(), equipment.getNote(), Files.readAllBytes(Paths.get(equipment.getImagePath())));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();
    }
}
