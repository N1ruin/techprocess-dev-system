package by.niruin.techprocessSystem.domain.controller;

import by.niruin.dto.equipment.TechnologicalEquipmentDto;
import by.niruin.techprocessSystem.domain.service.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import static by.niruin.techprocessSystem.constant.AlertInfoMessage.TECHNOLOGICAL_EQUIPMENT_ADDED_SUCCESSFULLY_MESSAGE;
import static by.niruin.techprocessSystem.constant.ScenePath.EQUIPMENT_MENU_PATH;
import static by.niruin.techprocessSystem.constant.SceneTitle.*;

@RestController
@RequiredArgsConstructor
public class CreateEquipmentController {
    @FXML
    private TextField indexField;
    @FXML
    private TextArea noteArea;
    @FXML
    private Button imageFindButton;
    @FXML
    private Button createButton;
    @FXML
    private Label imagePathLabel;

    private final TechnologicalEquipmentService technologicalEquipmentService;
    private final AlertService alertService;
    private final AsyncHelper asyncHelper;
    private final FileChooserService fileChooserService;
    private final SceneService sceneService;
    private BooleanProperty isPending;

    @FXML
    public void initialize() {
        isPending = new SimpleBooleanProperty(false);
        createButton.disableProperty().bind(indexField.textProperty().isEmpty()
                .or(noteArea.textProperty().isEmpty())
                .or(isPending));
    }

    @FXML
    public void findImage() {
        try {
            var file = fileChooserService.findImage(getCurrentStage(), 3);
            if (file != null) {
                imagePathLabel.setText(file.getAbsolutePath());
            }
        } catch (Exception e) {
            alertService.showErrorAlert(e);
        }
    }

    @FXML
    public void createEquipment() {
        var dto = new TechnologicalEquipmentDto(indexField.getText(), noteArea.getText(), imagePathLabel.getText());

        asyncHelper.executeAsyncNoResult(technologicalEquipmentService.addEquipment(dto),
                () -> {
                    alertService.showInfoAlert(SUCCESS_TITLE, TECHNOLOGICAL_EQUIPMENT_ADDED_SUCCESSFULLY_MESSAGE);
                    hideCurrentStage();
                    sceneService.openWindow(getCurrentStage(), EQUIPMENT_MENU_PATH, false, true, true);
                },
                alertService::showErrorAlert,
                isPending);
    }

    private void hideCurrentStage() {
        getCurrentStage().hide();
    }

    private Stage getCurrentStage() {
        return (Stage) createButton.getScene().getWindow();
    }
}
