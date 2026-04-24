package by.niruin.techprocessSystem.ui.service;

import by.niruin.techprocessSystem.ui.controller.Cleanable;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SceneManager {
    @Autowired
    private ApplicationContext context;

    public void openWindow(Stage currentStage, String fxmlPath, boolean modality, boolean resizable) {
        try {
            FXMLLoader loader = context.getBean(FXMLLoader.class);
            loader.setLocation(getClass().getResource(fxmlPath));

            Parent parent = loader.load();

            var controller = loader.getController();
            if (controller instanceof Cleanable cleanable) {
                cleanable.clear();
            }

            if (modality) {
                openNewWindow(parent, resizable);
            } else {
                rebuildWindow(currentStage, parent, resizable);
            }
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    private void openNewWindow(Parent parent, boolean resizable) {
        var newStage = new Stage();

        newStage.setScene(new Scene(parent));
        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.setResizable(resizable);
        newStage.show();
    }

    private void rebuildWindow(Stage currentStage, Parent parent, boolean resizable) {
        currentStage.setScene(new Scene(parent));
        currentStage.setResizable(resizable);
        currentStage.show();
    }
}
