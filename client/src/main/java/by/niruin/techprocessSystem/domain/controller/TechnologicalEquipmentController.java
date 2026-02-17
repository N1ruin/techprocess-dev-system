package by.niruin.techprocessSystem.domain.controller;

import by.niruin.dto.equipment.GetEquipmentsRequest;
import by.niruin.entity.TechnologicalEqupment;
import by.niruin.techprocessSystem.domain.service.SceneService;
import by.niruin.techprocessSystem.domain.service.TechnologicalEquipmentService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
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

    private TechnologicalEqupment equpment = new TechnologicalEqupment("111-111", "Тестовая оснастка", null);

    @FXML
    public void initialize() {
        indexColumn.setCellValueFactory(new PropertyValueFactory<>("index"));
        noteColumn.setCellValueFactory(new PropertyValueFactory<>("note"));
        sketchColumn.setCellValueFactory(new PropertyValueFactory<>("imagePath"));

        sketchColumn.setCellFactory(cell -> new TableCell<>() {
            private final ImageView imageView = new ImageView();
            {
                imageView.setFitWidth(60);
                imageView.setFitHeight(100);
                imageView.setPreserveRatio(true);
                setAlignment(Pos.CENTER);
                setOnMouseClicked(event -> {
                    if (event.getButton() == MouseButton.SECONDARY && !isEmpty() && getItem() != null) {
                        showFullImage(getItem());
                    }
                });
            }

            private void showFullImage(String path) {
                var stage = new Stage();
                stage.setTitle("Просмотр эскиза");

                var fullImage = new ImageView(new Image(path));
                fullImage.setPreserveRatio(true);
                fullImage.setFitWidth(800);
                fullImage.setFitHeight(600);

                ScrollPane scrollPane = new ScrollPane(fullImage);
                scrollPane.setFitToHeight(true);
                scrollPane.setFitToWidth(true);

                scrollPane.setOnScroll(event -> {
                    if (event.getDeltaY() != 0) {
                        // Коэффициент масштабирования (1.1 для увеличения, 0.9 для уменьшения)
                        double zoomFactor = event.getDeltaY() > 0 ? 1.1 : 0.9;

                        double newWidth = fullImage.getFitWidth() * zoomFactor;
                        double newHeight = fullImage.getFitHeight() * zoomFactor;

                        // Ограничения, чтобы не сделать картинку слишком маленькой или огромной
                        if (newWidth > 100 && newWidth < 4000) {
                            fullImage.setFitWidth(newWidth);
                            fullImage.setFitHeight(newHeight);
                        }

                        // Поглощаем событие, чтобы прокрутка (scroll) не двигала ползунки
                        event.consume();
                    }
                });
                Scene scene = new Scene(scrollPane, 820, 620);
                stage.setScene(scene);
                stage.show();
            }

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

        table.setItems(FXCollections.observableList(List.of(equpment)));
//        loadLastTenCreatedEquipments();
    }

    @FXML
    public void addEquipment() {
        //Открываем новое окно модальное с формой заполнения оснастки

    }

    @FXML
    public void updateEquipment() {
        var selectedEquipment = table.getSelectionModel().getSelectedItem();

        if (selectedEquipment == null) {
            return;
        }
        var newNote = "123";
        var chooser = new FileChooser();
        chooser.setTitle("Выберите файл эскиза");
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Изображения", "*.png", "*.jpg", "*.jpeg"));
        var file = chooser.showOpenDialog(indexSearchField.getScene().getWindow());
        if(file != null) {
            selectedEquipment.setImagePath(file.toURI().toString());
        }
        table.refresh();
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
