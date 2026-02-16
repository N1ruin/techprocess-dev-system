package by.niruin.techprocessSystem.domain.service;

import by.niruin.dto.techprocess.CreateTechprocessRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class TechprocessService {
    private final RestClient restClient;

    @Async
    public CompletableFuture<Void> createTechprocess(CreateTechprocessRequest request) {
        try {
            restClient.post()
                    .uri("/api/techprocesses")
                    .body(request)
                    .retrieve()
                    .toBodilessEntity();
            return CompletableFuture.completedFuture(null);
        } catch (HttpClientErrorException e) {
            return CompletableFuture.failedFuture(e);
        }
    }
}
