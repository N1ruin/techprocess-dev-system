package by.niruin.techprocessSystem.domain.validation;

import by.niruin.techprocessSystem.domain.dto.RegistrationRequest;
import jakarta.validation.Validator;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.event.KeyEvent;

@Service
public class TextFieldValidationService {
    @Autowired
    private Validator validator;

    public void validateTextField(KeyEvent event, Object object) {
        var textField = (TextField) event.getSource();

        var violations = validator.validate(object);
        textField.setStyle("");

        var fieldName = textField.getId().replace("Field", "");
    }
}
