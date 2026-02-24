package by.niruin.techprocessSystem.domain.service;

import by.niruin.dto.equipment.GetEquipmentsRequest;
import by.niruin.dto.equipment.GetTenEquipmentsResponse;
import by.niruin.dto.equipment.TechnologicalEquipmentDto;
import by.niruin.techprocessSystem.domain.entity.ApplicationSession;
import javafx.scene.control.Alert;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static by.niruin.techprocessSystem.util.javaFx.AlertUtil.showAlert;

@Service
@RequiredArgsConstructor
public class TechnologicalEquipmentService {
    private final RestClient restClient;
    private final ApplicationSession applicationSession;

    @Async
    public CompletableFuture<List<GetTenEquipmentsResponse>> getEquipmentsByIndexAndNote(GetEquipmentsRequest request) {
        try {
            restClient.post()
                    .uri("/api/equipments")
                    .body(request)
                    .retrieve()
                    .toBodilessEntity();
            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    @Async
    public CompletableFuture<Void> addEquipment(TechnologicalEquipmentDto dto) {
        try {
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("dto", dto);
            File imageFile = new File(dto.getImagePath());

            if (!imageFile.exists() || imageFile.isDirectory()) {
                throw new IllegalArgumentException("Выбран неверный файл или это директория!");
            }

            body.add("image", new FileSystemResource(imageFile));

            restClient.post()
                    .uri("/api/equipments/add")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(body)
                    .retrieve()
                    .toBodilessEntity();
            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            showAlert("Ошибка!", e.getMessage(), Alert.AlertType.ERROR);
            return CompletableFuture.failedFuture(e);
        }
    }

    public CompletableFuture<List<GetTenEquipmentsResponse>> findLastTenCreatedEquipments() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return restClient.get()
                        .uri("/api/equipments/last-ten")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer  " + applicationSession.getAccessToken())
                        .retrieve()
                        .body(new ParameterizedTypeReference<List<GetTenEquipmentsResponse>>() {
                        });
            } catch (Exception e) {
                showAlert("Ошибка!", e.getMessage(), Alert.AlertType.ERROR);
                return List.of();
            }
        });
    }
}

