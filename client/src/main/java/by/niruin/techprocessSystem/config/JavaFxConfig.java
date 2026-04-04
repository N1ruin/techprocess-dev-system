package by.niruin.techprocessSystem.config;

import javafx.fxml.FXMLLoader;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@AllArgsConstructor
public class JavaFxConfig {
    private final ApplicationContext context;

    @Bean
    @Scope("prototype")
    public FXMLLoader fxmlLoader() {
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(context::getBean);

        return loader;
    }
}
