package by.niruin.techprocessSystem.domain.repository;

import by.niruin.techprocessSystem.domain.entity.TechnologicalEquipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TechnologicalEquipmentRepository extends JpaRepository<TechnologicalEquipment, Long> {
    List<TechnologicalEquipment> findTop10ByOrderByAddedDateDesc();
}
