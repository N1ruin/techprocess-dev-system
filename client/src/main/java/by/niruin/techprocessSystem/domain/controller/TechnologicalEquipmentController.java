package by.niruin.techprocessSystem.domain.controller;

import by.niruin.entity.TechnologicalEqupment;
import by.niruin.techprocessSystem.domain.service.SceneService;
import by.niruin.techprocessSystem.domain.service.TechnologicalEquipmentService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TechnologicalEquipmentController {
    private final SceneService sceneService;
    private final TechnologicalEquipmentService equipmentService;

    @FXML
    private Button addEquipmentButton;
    @FXML
    private Button updateEquimpentButton;
    @FXML
    private Button goBackButton;
    @FXML
    private TextField indexSearchField;
    @FXML
    private TextField noteSearchField;
    @FXML
    private Button searchButton;
    @FXML
    private TableView<TechnologicalEqupment> table;
    @FXML
    private TableColumn<TechnologicalEqupment, String> indexColumn;
    @FXML
    private TableColumn<TechnologicalEqupment, String> noteColumn;
    @FXML
    private TableColumn<TechnologicalEqupment, String> sketchColumn;

    @FXML
    public void initialize() {
        indexColumn.setCellValueFactory(new PropertyValueFactory<>("index"));
        indexColumn.setCellValueFactory(new PropertyValueFactory<>("note"));
        indexColumn.setCellValueFactory(new PropertyValueFactory<>("imagePath"));

        sketchColumn.setCellFactory(cell -> new TableCell<>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(String path, boolean empty) {
                super.updateItem(path, empty);
                if (empty || path == null) {
                    setGraphic(null);
                } else {
                    var image = new Image(path, 100, 60, true, true);
                    imageView.setImage(image);
                    setGraphic(imageView);
                }
            }
        });
    }

    @FXML
    public void addEquipment() {

    }

    @FXML
    public void updateEquipment() {

    }

    @FXML
    public void goBack() {
        var stage = (Stage) addEquipmentButton.getScene().getWindow();
        sceneService.openWindow(stage, "/scene/mainScene.fxml", false, true);
    }

    @FXML
    public void search() {
        var indexText = indexColumn.getText();
        var noteText = noteColumn.getText();

    }
}
