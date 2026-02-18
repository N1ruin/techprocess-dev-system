package by.niruin.techprocessSystem.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "technological_equimpent")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class TechnologicalEquipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "equipment_index", nullable = false, unique = true, length = 20)
    private String equipmentIndex;

    @Column(name = "note")
    private String note;

    @Column(name = "image_path", nullable = false, unique = true)
    private String imagePath;

    @Column(name = "added_date", nullable = false)
    private LocalDateTime addedDate;
}