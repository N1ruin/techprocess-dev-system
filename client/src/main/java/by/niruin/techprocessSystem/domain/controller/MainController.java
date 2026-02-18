package by.niruin.techprocessSystem.domain.controller;

import by.niruin.techprocessSystem.domain.service.AuthenticationService;
import by.niruin.techprocessSystem.domain.service.SceneService;
import by.niruin.techprocessSystem.domain.service.TechprocessService;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Scope("prototype")
@RequiredArgsConstructor
public class MainController {
    private final TechprocessService techprocessService;
    private final SceneService sceneService;
    private final AuthenticationService authenticationService;

    @FXML
    private BorderPane contentArea;

    @FXML
    private Button createNewTechprocessButton;

    @FXML
    private Button technologicalToolsButton;

    @FXML
    private Button logOutButton;

    @FXML
    public void initialize() {
        updateCenterContainer("/scene/processList.fxml");
    }

    @FXML
    public void openToolsMenu() {
        var stage = (Stage) createNewTechprocessButton.getScene().getWindow();
        sceneService.openWindow(stage, "/scene/equipmentMenu.fxml", false, true, true);
    }

    @FXML
    public void logOut() {
        authenticationService.logout();
        var stage = (Stage) createNewTechprocessButton.getScene().getWindow();
        sceneService.openWindow(stage, "/scene/startScene.fxml", false, false, false);
    }

    private void updateCenterContainer(String fxmlPath) {
        Parent view = sceneService.loadView(fxmlPath);
        if (view != null) {
            contentArea.setCenter(view);
        }
    }
}
