package by.niruin.techprocessSystem;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "by.niruin.techprocessSystem")
public class Main {
    public static void main(String[] args) {
        Application.launch(TechprocessSystemApplication.class);
    }
}