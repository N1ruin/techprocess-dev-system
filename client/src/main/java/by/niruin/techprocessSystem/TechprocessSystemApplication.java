package by.niruin.techprocessSystem;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import static by.niruin.techprocessSystem.constant.ScenePath.START_SCENE_PATH;
import static by.niruin.techprocessSystem.constant.SceneTitle.APPLICATION_TITLE;

public class TechprocessSystemApplication extends Application {
    private ConfigurableApplicationContext context;

    @Override
    public void init() {
        String[] args = getParameters().getRaw().toArray(new String[0]);

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
        loader.setLocation(getClass().getResource(START_SCENE_PATH));

        return loader.load();
    }

    private void runStartScene(Stage firstStage, Parent parent) {
        var imageIconStream = getClass().getResourceAsStream("./scene/image/add-document.png");

        if (imageIconStream != null) {
            firstStage.getIcons().add(new Image(imageIconStream));
        }

        firstStage.setTitle(APPLICATION_TITLE);
        firstStage.setScene(new Scene(parent));
        firstStage.setResizable(false);
        firstStage.show();
    }
}
