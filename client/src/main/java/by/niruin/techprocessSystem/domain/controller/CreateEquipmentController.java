package by.niruin.techprocessSystem.domain.controller;

import by.niruin.dto.equipment.TechnologicalEquipmentDto;
import by.niruin.techprocessSystem.domain.service.TechnologicalEquipmentService;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import static by.niruin.techprocessSystem.util.javaFx.AlertUtil.showAlert;

@RestController
@RequiredArgsConstructor
public class CreateEquipmentController {
    private final TechnologicalEquipmentService technologicalEquipmentService;
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

    private final BooleanProperty isPending = new SimpleBooleanProperty(false);

    @FXML
    public void initialize() {
        createButton.disableProperty().bind(indexField.textProperty().isEmpty()
                .or(noteArea.textProperty().isEmpty())
                .or(isPending));
    }

    @FXML
    public void findImage() {
        var fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите файл эскиза");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Изображения", "*.png", "*.jpg", "*.jpeg"));
        var file = fileChooser.showOpenDialog(imageFindButton.getScene().getWindow());
        if (file != null) {
            var bytes = file.length();
            var megabytes = bytes / (1024 * 1024);
            if (megabytes <= 3) {
                imagePathLabel.setText(file.getAbsolutePath());
            } else {
                showAlert("Ошибка", "Размер файла не может превышать 3 мегабайта!", Alert.AlertType.INFORMATION);
            }
        }
    }

    @FXML
    public void createEquipment() {
        isPending.set(true);
        var dto = new TechnologicalEquipmentDto(indexField.getText(), noteArea.getText(), imagePathLabel.getText());
        technologicalEquipmentService.addEquipment(dto)
                .thenAccept(action -> Platform.runLater(() -> showAlert("Успех!", "Оснастка с индексом %s успешно добавлена в базу данных!", Alert.AlertType.INFORMATION)))
                .exceptionally(exception -> {
                    Platform.runLater(() -> {
                        showAlert("Ошибка!", exception.getCause().getMessage(), Alert.AlertType.ERROR);
                        isPending.set(false);
                    });
                    return null;
                });
        var stage = createButton.getScene().getWindow();
        stage.hide();
    }
}
