/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package PoolGame;

import java.util.List;

import PoolGame.Singleton.ConfigReader;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import javafx.util.Duration;

/** The JavaFX application */
public class App extends Application {

    private final double FRAMETIME = 1.0 / 60.0;

    /**
     * Loading the ConfigReader object from given filepath
     * @param args CLI arguments
     * @return The ConfigReader object
     */
    public static ConfigReader loadConfig(List<String> args) {
        String configPath;
        boolean isResourcesDir = false;
		if (args.size() > 0) {
			configPath = args.get(0);
		} else {
//			 configPath = "src/main/resources/config.json";
			configPath = "/config_easy.json";
            isResourcesDir = true;
		}

        ConfigReader config = ConfigReader.getInstance(configPath, isResourcesDir);
        return config;
    }

    @Override
    public void start(Stage stage) {
        Group root = new Group();
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setTitle("PoolGame");
        stage.show();
        
        ConfigReader config = loadConfig(getParameters().getRaw());
        Game game = new Game(config);
        
        Canvas canvas = new Canvas(game.getWindowDimX(), game.getWindowDimY());
        root.getChildren().add(canvas);
        // GraphicsContext gc = canvas.getGraphicsContext2D();
        game.addDrawables(root);
        
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        KeyFrame frame = new KeyFrame(Duration.seconds(FRAMETIME),
        (actionEvent) -> {
                game.tick();
            });

        timeline.getKeyFrames().add(frame);
        timeline.play();
    }

    /**
     * The entry point of the program
     * @param args CLI arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
