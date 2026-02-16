package by.niruin.techprocessSystem.domain.controller;

import by.niruin.dto.equipment.GetEquipmentsRequest;
import by.niruin.entity.TechnologicalEqupment;
import by.niruin.techprocessSystem.domain.service.SceneService;
import by.niruin.techprocessSystem.domain.service.TechnologicalEquipmentService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.List;

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

    private File selectedImage;

    @FXML
    public void initialize() {
        indexColumn.setCellValueFactory(new PropertyValueFactory<>("index"));
        noteColumn.setCellValueFactory(new PropertyValueFactory<>("note"));
        sketchColumn.setCellValueFactory(new PropertyValueFactory<>("imagePath"));

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

        table.setItems(FXCollections.observableList(List.of(new TechnologicalEqupment("111-111", "Тестовая оснастка", null))));
//        loadLastTenCreatedEquipments();
    }

    @FXML
    public void addEquipment() {
        //Открываем новое окно модальное с формой заполнения оснастки

    }

    @FXML
    public void updateEquipment() {
        var selectedEquipment = table.getSelectionModel().getSelectedItem();

        if(selectedEquipment == null) {
            return;
        }

        String newNote = "123";
        FileChooser chooser = new FileChooser();
        File file = chooser.showOpenDialog(indexSearchField.getScene().getWindow());
        System.out.println(file.getAbsolutePath());
    }

    @FXML
    public void goBack() {
        var stage = (Stage) addEquipmentButton.getScene().getWindow();
        sceneService.openWindow(stage, "/scene/mainScene.fxml", false, true);
    }

    @FXML
    public void search() {
        var indexText = indexSearchField.getText();
        var noteText = noteSearchField.getText();

        var request = new GetEquipmentsRequest(indexText, noteText);

        equipmentService.getEquipmentsByIndexAndNote(request)
                .thenAccept(filteredData -> {
                    Platform.runLater(() -> {
                        if (filteredData != null) {
                            table.setItems(FXCollections.observableList(filteredData));
                        }
                    });
                });
    }

    private void loadLastTenCreatedEquipments() {
        equipmentService.findLastTenCreatedEquipments()
                .thenAccept(equipments -> {
                    Platform.runLater(() -> {
                        if (equipments != null) {
                            table.setItems(FXCollections.observableList(equipments));
                        }
                    });
                })
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    return null;
                });
    }
}
