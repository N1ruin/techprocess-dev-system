package by.niruin.techprocessSystem.domain.controller;

import by.niruin.dto.auth.AuthenticationRequest;
import by.niruin.techprocessSystem.domain.service.AlertService;
import by.niruin.techprocessSystem.domain.service.AsyncHelper;
import by.niruin.techprocessSystem.domain.service.AuthenticationService;
import by.niruin.techprocessSystem.domain.service.SceneService;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import static by.niruin.techprocessSystem.constant.ScenePath.MAIN_SCENE_PATH;
import static by.niruin.techprocessSystem.constant.ScenePath.REGISTRATION_SCENE_PATH;


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
    private final AsyncHelper asyncHelper;
    private final AlertService alertService;
    private BooleanProperty isLogging;

    @FXML
    public void initialize() {
        isLogging = new SimpleBooleanProperty(false);
        signInButton.disableProperty().bind(loginField.textProperty().isEmpty()
                .or(passwordField.textProperty().isEmpty())
                .or(isLogging));
        enterTestData();
    }

    @FXML
    public void signIn() {
        isLogging.set(true);
        var authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setLogin(loginField.getText());
        authenticationRequest.setPassword(passwordField.getText());

        asyncHelper.executeAsyncNoResult(authenticationService.signIn(authenticationRequest),
                this::showMainScene, alertService::showErrorAlert, isLogging);
    }

    @FXML
    public void signUp() {
        showRegistrationScene();
    }

    private void showMainScene() {
        Platform.runLater(() ->
                sceneService.openWindow(sceneService.getElementStage(signInButton), MAIN_SCENE_PATH, false, true, true));
    }

    private void showRegistrationScene() {
        sceneService.openWindow(sceneService.getElementStage(signInButton), REGISTRATION_SCENE_PATH, false, false, false);
    }
    private void enterTestData() {
        loginField.setText("elfagun");
        passwordField.setText("12312q");
    }
}
