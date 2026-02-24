package by.niruin.techprocessSystem.domain.service;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@Service
public class AsyncHelper {
    public <T> void executeAsync(CompletableFuture<T> future, Consumer<T> onSuccess, Consumer<Throwable> onFailure, BooleanProperty isExecutableButtonBlocked) {
        if (isExecutableButtonBlocked != null) {
            isExecutableButtonBlocked.set(true);
        }

        future.handle((result, exception) -> {
            Platform.runLater(() -> {
                if (isExecutableButtonBlocked != null) {
                    isExecutableButtonBlocked.set(false);
                }

                if (exception != null) {
                    onFailure.accept(exception);
                } else {
                    onSuccess.accept(result);
                }

            });

            return null;
        });
    }

    public <T> void executeAsyncNoResult(CompletableFuture<T> future, Runnable
            onSuccess, Consumer<Throwable> onFailure, BooleanProperty isExecutableButtonBlocked) {
        executeAsync(future, result -> onSuccess.run(), onFailure, isExecutableButtonBlocked);
    }
}
