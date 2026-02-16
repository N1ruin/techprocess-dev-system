package by.niruin.techprocessSystem.domain.service;

import by.niruin.dto.equipment.GetEquipmentsRequest;
import by.niruin.entity.TechnologicalEqupment;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class TechnologicalEquipmentService {
    private final RestClient restClient;

    @Async
    public CompletableFuture<List<TechnologicalEqupment>> getEquipmentsByIndexAndNote(GetEquipmentsRequest request) {
        try {
            restClient.post()
                    .uri("/api/equipments")
                    .body(request)
                    .retrieve()
                    .toBodilessEntity();
            return CompletableFuture.completedFuture(null);
        } catch (HttpClientErrorException e) {
            return CompletableFuture.failedFuture(e);
        }
    }
}
