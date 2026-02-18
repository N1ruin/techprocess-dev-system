package by.niruin.techprocessSystem.domain.controller;

import by.niruin.dto.auth.AuthenticationRequest;
import by.niruin.techprocessSystem.domain.service.AuthenticationService;
import by.niruin.techprocessSystem.domain.service.SceneService;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import static by.niruin.techprocessSystem.util.javaFx.AlertUtil.showAlert;

@RestController
@Scope("prototype")
@RequiredArgsConstructor
public class AuthenticationController {
    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button signInButton;
    @FXML
    private Button signUpButton;

    private final AuthenticationService authenticationService;
    private final SceneService sceneService;

    private BooleanProperty isLogging = new SimpleBooleanProperty(false);

    @FXML
    public void initialize() {

        signInButton.disableProperty().bind(loginField.textProperty().isEmpty()
                .or(passwordField.textProperty().isEmpty())
                .or(isLogging));

        loginField.setText("elagun");
        passwordField.setText("12312q");
    }

    @FXML
    public void signIn() {
        isLogging.set(true);
        var login = loginField.getText();
        var password = passwordField.getText();
        var authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setLogin(login);
        authenticationRequest.setPassword(password);

        authenticationService.signIn(authenticationRequest)
                .thenAccept(response -> {
                    Platform.runLater(() -> sceneService.openWindow(getCurrentStage(), "/scene/mainScene.fxml", false, true, true));
                    isLogging.set(false);
                })
                .exceptionally(exception -> {
                    Platform.runLater(() -> {
                        isLogging.set(false);
                        showAlert("Ошибка", "Сервер не отвечает, попробуйте позже", Alert.AlertType.ERROR);
                    });
                    return null;
                });
    }

    @FXML
    public void signUp() {
        sceneService.openWindow(getCurrentStage(), "/scene/registrationScene.fxml", false, false, false);
    }

    private Stage getCurrentStage() {
        return (Stage) signInButton.getScene().getWindow();
    }
}
