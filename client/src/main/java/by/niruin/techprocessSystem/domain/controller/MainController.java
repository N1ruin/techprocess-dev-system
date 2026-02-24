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

import static by.niruin.techprocessSystem.constant.ScenePath.*;


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
        updateCenterContainer();
    }

    @FXML
    public void openToolsMenu() {
        var stage = (Stage) createNewTechprocessButton.getScene().getWindow();
        sceneService.openWindow(stage, EQUIPMENT_MENU_PATH, false, true, true);
    }

    @FXML
    public void logOut() {
        authenticationService.logout();
        var stage = (Stage) createNewTechprocessButton.getScene().getWindow();
        sceneService.openWindow(stage, START_SCENE_PATH, false, false, false);
    }

    private void updateCenterContainer() {
        Parent view = sceneService.loadView(PROCESS_LIST_PATH);
        if (view != null) {
            contentArea.setCenter(view);
        }
    }
}
