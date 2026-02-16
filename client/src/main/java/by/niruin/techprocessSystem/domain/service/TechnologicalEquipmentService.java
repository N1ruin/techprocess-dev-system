package by.niruin.techprocessSystem.domain.service;

import by.niruin.dto.equipment.GetEquipmentsRequest;
import by.niruin.entity.TechnologicalEqupment;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
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

    public void add() {
    }

    public CompletableFuture<List<TechnologicalEqupment>> findLastTenCreatedEquipments() {
        return CompletableFuture.supplyAsync(() -> {
            try {
               return restClient.get()
                        .uri("/api/equipments/last-ten")
                        .retrieve()
                        .body(new ParameterizedTypeReference<List<TechnologicalEqupment>>() {
                        });
            } catch (Exception e) {
                e.printStackTrace();
                return List.of();
            }
        });
    }
    }

