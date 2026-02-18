package by.niruin.techprocessSystem.domain.controller;

import by.niruin.dto.techprocess.TechProcessDto;
import by.niruin.techprocessSystem.domain.service.SceneService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@Scope("prototype")
public class TechprocessListController {
    private final SceneService sceneService;
    @FXML
    private TextField searchField;
    @FXML
    private TextField developerSearchField;
    @FXML
    private TextField baseSearchField;
    @FXML
    private Button updateTechprocessButton;
    @FXML
    private Button refreshButton;
    @FXML
    private TableView<TechProcessDto> processTable;
    @FXML
    private TableColumn<TechProcessDto, String> colInventoryNumber;
    @FXML
    private TableColumn<TechProcessDto, String> colPartNumber;
    @FXML
    private TableColumn<TechProcessDto, String> colPartName;
    @FXML
    private TableColumn<TechProcessDto, String> colBase;
    @FXML
    private TableColumn<TechProcessDto, String> colTechprocessType;
    @FXML
    private TableColumn<TechProcessDto, String> colWorkType;
    @FXML
    private TableColumn<TechProcessDto, String> colDeveloper;
    @FXML
    private TableColumn<TechProcessDto, String> colCreatingDate;
    @FXML
    private TableColumn<TechProcessDto, String> colStatus;

    @FXML
    public void initialize() {
        colInventoryNumber.setCellValueFactory(new PropertyValueFactory<>("colInventoryNumber"));
        colPartNumber.setCellValueFactory(new PropertyValueFactory<>("colPartNumber"));
        colPartName.setCellValueFactory(new PropertyValueFactory<>("colPartName"));
        colBase.setCellValueFactory(new PropertyValueFactory<>("colBase"));
        colTechprocessType.setCellValueFactory(new PropertyValueFactory<>("colTechprocessType"));
        colWorkType.setCellValueFactory(new PropertyValueFactory<>("colWorkType"));
        colDeveloper.setCellValueFactory(new PropertyValueFactory<>("colDeveloper"));
        colCreatingDate.setCellValueFactory(new PropertyValueFactory<>("colCreatingDate"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("colStatus"));
    }

    @FXML
    public void updateTechprocess() {

    }

    @FXML
    public void refreshTable() {

    }
}
