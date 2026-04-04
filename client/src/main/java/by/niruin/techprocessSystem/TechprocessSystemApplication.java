package by.niruin.techprocessSystem;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class TechprocessSystemApplication extends Application {
    public static final String APPLICATION_TITLE = "Techprocess system";
    private ConfigurableApplicationContext context;

    @Override
    public void init() {
        String[] args = getParameters()
                .getRaw()
                .toArray(new String[0]);

        context = new SpringApplicationBuilder(Main.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }

    @Override
    public void start(Stage firstStage) throws Exception {
        Parent parent = loadScene();
        runStartScene(firstStage, parent);
    }

    @Override
    public void stop() {
        context.close();
        Platform.exit();
    }

    private Parent loadScene() throws Exception {
        var loader = context.getBean(FXMLLoader.class);
        loader.setLocation(getClass().getResource("/scene/startScene.fxml"));

        return loader.load();
    }

    private void runStartScene(Stage firstStage, Parent parent) {
        firstStage.setTitle(APPLICATION_TITLE);
        firstStage.setScene(new Scene(parent));
        firstStage.setResizable(false);
        firstStage.show();
    }
}
