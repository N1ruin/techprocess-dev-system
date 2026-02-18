package by.niruin.techprocessSystem.domain.controller;

import by.niruin.dto.equipment.TechnologicalEquipmentDto;
import by.niruin.techprocessSystem.domain.service.TechnologicalEquipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/equipments")
public class TechnologicalEquipmentController {
    private final TechnologicalEquipmentService service;

    @PostMapping("/add")
    public void addEquipment(@RequestBody TechnologicalEquipmentDto dto) {

    }

    @GetMapping("/last-ten")
    public ResponseEntity<List<TechnologicalEquipmentDto>> getLastTenEquipments() {
        return ResponseEntity.ok(service.getLastTenEquipment());
    }
}
