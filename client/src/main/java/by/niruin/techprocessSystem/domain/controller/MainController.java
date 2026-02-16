package by.niruin.techprocessSystem.domain.controller;

import by.niruin.techprocessSystem.domain.service.SceneService;
import by.niruin.techprocessSystem.domain.service.TechprocessService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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

    @FXML
    private Button createNewTechprocessButton;

    @FXML
    private Button technologicalToolsButton;

    @FXML
    public void openToolsMenu() {
        var stage = (Stage) createNewTechprocessButton.getScene().getWindow();
        sceneService.openWindow(stage, "/scene/equipmentMenu.fxml", false, true);
    }

}
