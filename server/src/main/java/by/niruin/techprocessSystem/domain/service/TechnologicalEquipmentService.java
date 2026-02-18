package by.niruin.techprocessSystem.domain.service;

import by.niruin.dto.equipment.TechnologicalEquipmentDto;
import by.niruin.techprocessSystem.domain.entity.TechnologicalEquipment;
import by.niruin.techprocessSystem.domain.repository.TechnologicalEquipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TechnologicalEquipmentService {
    private final TechnologicalEquipmentRepository technologicalEquipmentRepository;

    public List<TechnologicalEquipmentDto> getLastTenEquipment(){
        var equipments = technologicalEquipmentRepository.findTop10ByOrderByAddedDateDesc();

        return equipments.stream()
                .map(equipment -> new TechnologicalEquipmentDto(equipment.getEquipmentIndex(), equipment.getNote(), equipment.getImagePath()))
                .toList();
    }

}
