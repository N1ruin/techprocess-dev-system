package by.niruin.techprocessSystem.domain.controller;

import by.niruin.dto.RegistrationRequest;
import by.niruin.techprocessSystem.domain.service.RegistrationService;
import by.niruin.techprocessSystem.domain.service.SceneManager;
import by.niruin.techprocessSystem.exception.UnknownControlException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RegistrationController implements Cleanable {
    @FXML
    private TextField login;
    @FXML
    private PasswordField password;
    @FXML
    private TextField passwordTextField;
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField fatherName;
    @FXML
    private DatePicker birthDate;
    @FXML
    private Button signUpButton;
    @FXML
    private Button goBackButton;
    @FXML
    private Button eye;
    @FXML
    private ImageView eyeIcon;

    private final RegistrationService registrationService;
    private final SceneManager sceneService;
    private final Validator validator;

    private boolean isPasswordVisible;

    @FXML
    public void initialize() {
        isPasswordVisible = false;
        passwordTextField.setVisible(false);

        birthDate.getEditor().setDisable(true);
        birthDate.getEditor().setOpacity(1);

        var fields = List.of(login, password, firstName, lastName, fatherName);

        for (var field : fields) {
            field.textProperty().addListener((observable, oldValue, newValue) -> {
                validateInput(field, field.getId(), field.getText());
            });
        }
        birthDate.valueProperty().addListener((observable, oldValue, newValue) -> {
            validateInput(birthDate, birthDate.getId(), birthDate.getValue());
        });

        passwordTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (isPasswordVisible) {
                password.setText(newValue);
                validateInput(passwordTextField, password.getId(), newValue);
            }
        });
    }

    @FXML
    public void signUp() {
        signUpButton.setDisable(true);

        var registrationRequest = buildRegistrationRequest();

        var violations = validator.validate(registrationRequest);

        if (!violations.isEmpty()) {
            highlightAllFields(violations);
            showAlert("Ошибка валидации", " Пожалуйста, исправьте отмеченные поля!", Alert.AlertType.ERROR);
            signUpButton.setDisable(false);
            return;
        }

        registrationService.signUp(registrationRequest)
                .thenAccept(result -> Platform.runLater(() -> {
                    showAlert("Успех!", "Вы успешно зарегистрированы!", Alert.AlertType.INFORMATION);
                }))
                .exceptionally(exception -> {
                    Platform.runLater(() -> {
                        showAlert("Ошибка регистрации", "Проверьте данные: " + exception.getMessage(), Alert.AlertType.ERROR);
                        signUpButton.setDisable(false);
                    });
                    return null;
                });

        var stage = (Stage) login.getScene().getWindow();
        sceneService.openWindow(stage, "/scene/startScene.fxml", false, false);
    }

    @FXML
    public void goBack() {
        var stage = (Stage) login.getScene().getWindow();

        sceneService.openWindow(stage, "/scene/startScene.fxml", false, false);
    }

    @FXML
    public void togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible;

        if (!isPasswordVisible) {
            password.setText(passwordTextField.getText());
            password.setVisible(true);
            passwordTextField.setVisible(false);
            validateInput(password, password.getId(), password.getText());
            eyeIcon.setImage(new Image("/scene/image/passwordButton.png"));
        } else {
            passwordTextField.setText(password.getText());
            passwordTextField.setVisible(true);
            password.setVisible(false);
            validateInput(passwordTextField, password.getId(), password.getText());
            eyeIcon.setImage(new Image("/scene/image/passwordButtonNoLine.png"));
        }
    }

    public void clear() {
        List<TextInputControl> textControls = List.of(
                login, password, passwordTextField,
                firstName, lastName, fatherName
        );

        textControls.forEach(control -> {
            control.clear();
            resetControlStyle(control);
        });

        birthDate.setValue(null);
        resetControlStyle(birthDate);

        isPasswordVisible = false;
        password.setVisible(true);
        passwordTextField.setVisible(false);
        eyeIcon.setImage(new Image("/scene/image/passwordButton.png"));

        signUpButton.setDisable(false);

        login.requestFocus();
    }

    private void highlightAllFields(Set<ConstraintViolation<RegistrationRequest>> violations) {
        List.of(login, password, firstName, lastName, fatherName, birthDate).forEach(control -> {
            var controlValue = getControlValue(control);

            validateInput(control, control.getId(), controlValue);
        });
    }

    private void validateInput(Control control, String controlId, Object value) {
        var violations = validator.validateValue(RegistrationRequest.class, controlId, value);

        if (violations.isEmpty()) {
            control.setStyle("-fx-background-radius: 25; -fx-border-radius: 25; -fx-border-color: green; -fx-border-width: 2px;");
            control.setTooltip(null);
        } else {
            String errorMessage = getViolationsMessage(violations);
            control.setStyle("-fx-background-radius: 25; -fx-border-radius: 25; -fx-border-color: red; -fx-border-width: 2px;");
            Tooltip tooltip = new Tooltip(errorMessage);
            tooltip.setWrapText(true);
            tooltip.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white; -fx-font-size: 12px;");
            tooltip.setShowDelay(javafx.util.Duration.millis(100));

            control.setTooltip(tooltip);
        }
    }

    private RegistrationRequest buildRegistrationRequest() {
        String currentPassword = isPasswordVisible ? passwordTextField.getText() : password.getText();

        return RegistrationRequest.builder()
                .login(login.getText())
                .password(currentPassword)
                .firstName(firstName.getText())
                .lastName(lastName.getText())
                .surname(fatherName.getText())
                .birthDate(birthDate.getValue())
                .build();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String getViolationsMessage(Set<ConstraintViolation<RegistrationRequest>> violationSet) {
        return violationSet.stream()
                .map(ConstraintViolation::getMessage)
                .distinct()
                .collect(Collectors.joining("\n"));
    }

    private Object getControlValue(Control control) {
        if (control instanceof DatePicker datePicker) {
            return datePicker.getValue();
        }
        if (control instanceof PasswordField passwordField && isPasswordVisible) {
            return passwordField.getText();
        }
        if (control instanceof TextField textField) {
            return textField.getText();
        }
        throw new UnknownControlException("Unknown control %s class".formatted(control.getId()));
    }

    private void resetControlStyle(Control control) {
        control.setStyle("-fx-background-radius: 25; -fx-border-radius: 25;");
        control.setTooltip(null);
    }
}
