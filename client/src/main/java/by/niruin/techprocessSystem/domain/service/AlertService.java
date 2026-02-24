package by.niruin.techprocessSystem.domain.service;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import org.springframework.stereotype.Service;

import static by.niruin.techprocessSystem.constant.SceneTitle.ERROR_TITLE;


@Service
public class AlertService {

    public void showErrorAlert(Throwable throwable) {
        var message = getErrorMessage(throwable);
        showAlert(ERROR_TITLE, message, Alert.AlertType.ERROR);
    }

    public void showInfoAlert(String title, String info) {
        showAlert(title, info, Alert.AlertType.INFORMATION);
    }

    private String getErrorMessage(Throwable throwable) {
        var cause = throwable.getCause();

        return (cause != null) ? cause.getMessage() : throwable.getMessage();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Platform.runLater(() -> {
            Alert alert = new Alert(type);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}
