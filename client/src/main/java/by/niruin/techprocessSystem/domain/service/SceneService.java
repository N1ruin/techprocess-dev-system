package by.niruin.techprocessSystem.domain.service;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Control;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class SceneService {
    private ApplicationContext context;
    private final AlertService alertService;

    public void openWindow(Stage currentStage, String fxmlPath, boolean modality, boolean resizable, boolean maximized) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            loader.setControllerFactory(context::getBean);
            Parent parent = loader.load();

            if (modality) {
                Stage modalStage = new Stage();
                modalStage.setScene(new Scene(parent));
                modalStage.initModality(Modality.APPLICATION_MODAL);
                modalStage.initOwner(currentStage);
                modalStage.setResizable(resizable);
                modalStage.show();
            } else {
                if (currentStage == null) {
                    currentStage = new Stage();
                }

                if (currentStage.getScene() == null) {
                    currentStage.setScene(new Scene(parent));
                } else {
                    currentStage.getScene().setRoot(parent);
                }

                currentStage.setResizable(resizable);
                currentStage.setMaximized(maximized);

                if (!currentStage.isShowing()) {
                    currentStage.show();
                }

                if (!maximized) {
                    currentStage.sizeToScene();
                    currentStage.centerOnScreen();
                }
            }
        } catch (IOException e) {
            alertService.showErrorAlert(e);
        }
    }

    public Parent loadView(String fxmlPath) {
        try {
            var loader = context.getBean(FXMLLoader.class);
            loader.setLocation(getClass().getResource(fxmlPath));
            loader.setControllerFactory(context::getBean);
            return loader.load();
        } catch (IOException e) {
            alertService.showErrorAlert(e);
            return null;
        }
    }

    public Stage getElementStage(Control control) {
        return (Stage) control.getScene().getWindow();
    }
}
