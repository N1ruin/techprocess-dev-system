package by.niruin.techprocessSystem.domain.controller;

import by.niruin.dto.equipment.GetEquipmentsRequest;
import by.niruin.dto.equipment.GetTenEquipmentsResponse;
import by.niruin.techprocessSystem.domain.service.*;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static by.niruin.techprocessSystem.constant.ScenePath.CREATE_EQUIPMENT_PATH;
import static by.niruin.techprocessSystem.constant.ScenePath.MAIN_SCENE_PATH;

@RestController
@RequiredArgsConstructor
public class TechnologicalEquipmentController {
    private final SceneService sceneService;
    private final TechnologicalEquipmentService equipmentService;
    private final AlertService alertService;
    private final FileChooserService fileChooserService;
    private final AsyncHelper asyncHelper;
    private final ObservableList<GetTenEquipmentsResponse> tableData = FXCollections.observableArrayList();

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
    private TableView<GetTenEquipmentsResponse> table;
    @FXML
    private TableColumn<GetTenEquipmentsResponse, String> indexColumn;
    @FXML
    private TableColumn<GetTenEquipmentsResponse, String> noteColumn;
    @FXML
    private TableColumn<GetTenEquipmentsResponse, byte[]> sketchColumn;

    private BooleanProperty isSearching;

    @FXML
    public void initialize() {
        isSearching = new SimpleBooleanProperty(false);
        indexColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getIndex()));
        noteColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNote()));
        sketchColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getFileBytes()));

        setUpImageColumn();
        table.setItems(tableData);
        loadAllEquipments();
    }

    @FXML
    public void addEquipment() {
        var currentStage = sceneService.getElementStage(searchButton);
        sceneService.openWindow(currentStage, CREATE_EQUIPMENT_PATH, true, false, false);
    }

    @FXML
    public void updateEquipment() {
        var selectedIndex = table.getSelectionModel().getSelectedIndex();

        if (selectedIndex < 0) {
            return;
        }

        var selectedEquipment = table.getSelectionModel().getSelectedItem();
        updateEquipmentImage(selectedEquipment, selectedIndex);
    }


    @FXML
    public void goBack() {
        var stage = (Stage) addEquipmentButton.getScene().getWindow();
        sceneService.openWindow(stage, MAIN_SCENE_PATH, false, true, true);
    }

    @FXML
    public void search() {
        var request = new GetEquipmentsRequest(indexSearchField.getText(), noteSearchField.getText());

        asyncHelper.executeAsync(
                equipmentService.getEquipmentsByIndexAndNote(request),
                this::updateTableData,
                alertService::showErrorAlert,
                isSearching);
    }

    private void setUpImageColumn() {
        sketchColumn.setCellFactory(cell -> new TableCell<>() {
            private final ImageView imageView = new ImageView();

            {
                imageView.setFitHeight(80);
                imageView.setPreserveRatio(true);
                setAlignment(Pos.CENTER);
            }

            @Override
            protected void updateItem(byte[] imageData, boolean empty) {
                super.updateItem(imageData, empty);
                if (empty || imageData == null || imageData.length == 0) {
                    setGraphic(null);
                } else {
                    imageView.setImage(new Image(new ByteArrayInputStream(imageData)));
                    setGraphic(imageView);
                }
            }
        });
    }

    private void loadAllEquipments() {
        equipmentService.findLastTenCreatedEquipments()
                .thenAccept(equipments -> Platform.runLater(() -> {
                    if (equipments != null) {
                        tableData.setAll(equipments);
                    }
                }))
                .exceptionally(e -> {
                    alertService.showErrorAlert(e);
                    return null;
                });
    }

    private void updateEquipmentImage(GetTenEquipmentsResponse selectedEquipment, int selectedIndex) {
        var file = fileChooserService.findImage(sceneService.getElementStage(goBackButton), 3);
        if (file != null) {
            try {
                byte[] newBytes = Files.readAllBytes(file.toPath());
                var updated = new GetTenEquipmentsResponse(selectedEquipment.getIndex(), selectedEquipment.getNote(), newBytes);
                tableData.set(selectedIndex, updated);
            } catch (IOException e) {
                alertService.showErrorAlert(e);
            }
        }
    }

    private void updateTableData(List<GetTenEquipmentsResponse> data) {
        if (data != null) {
            tableData.setAll(data);
        }
    }
}
