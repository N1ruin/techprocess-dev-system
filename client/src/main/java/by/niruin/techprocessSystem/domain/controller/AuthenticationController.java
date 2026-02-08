package by.niruin.techprocessSystem.domain.controller;

import by.niruin.dto.AuthenticationRequest;
import by.niruin.techprocessSystem.domain.service.AuthenticationService;
import by.niruin.techprocessSystem.domain.service.SceneService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope("prototype")
public class AuthenticationController {
    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button signInButton;
    @FXML
    private Button signUpButton;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private SceneService sceneService;

    @FXML
    public void initialize() {
        signInButton.disableProperty().bind(loginField.textProperty().isEmpty().or(passwordField.textProperty().isEmpty()));
    }

    @FXML
    public void signIn() {
        var login = loginField.getText();
        var password = passwordField.getText();
        var authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setLogin(login);
        authenticationRequest.setPassword(password);

        authenticationService.signIn(authenticationRequest).thenAccept(result -> {
        }).exceptionally(exception -> {
            Platform.runLater(() -> showErrorAlert("Ошибка регистрации", "Проверьте данные: " + exception.getMessage()));
            return null;
        });
    }

    @FXML
    public void signUp() {
        var stage = (Stage) signInButton.getScene().getWindow();
        sceneService.openWindow(stage, "/scene/registrationScene.fxml", false, false);
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
