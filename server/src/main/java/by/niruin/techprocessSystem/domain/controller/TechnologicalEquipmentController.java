package by.niruin.techprocessSystem.domain.controller;

import by.niruin.dto.equipment.GetTenEquipmentsResponse;
import by.niruin.dto.equipment.TechnologicalEquipmentDto;
import by.niruin.techprocessSystem.domain.service.TechnologicalEquipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/equipments")
public class TechnologicalEquipmentController {
    private final TechnologicalEquipmentService service;

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> addEquipment(@RequestPart("dto") TechnologicalEquipmentDto dto, @RequestPart("image") MultipartFile image) {
        service.add(dto, image);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/last-ten")
    public ResponseEntity<List<GetTenEquipmentsResponse>> getLastTenEquipments() {
        return ResponseEntity.ok(service.getLastTenEquipment());
    }
}
