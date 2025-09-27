
package com.example.vlearn;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class BgTest extends Application {
    @Override
    public void start(Stage stage) {
        AnchorPane root = new AnchorPane();

        // Load image from resources (make sure img/7084.jpg is in src/main/resources/img)
        Image bgImage = new Image(getClass().getResource("/com/example/vlearn/img/7084.jpg").toExternalForm());

        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(
                bgImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                backgroundSize
        );
        root.setBackground(new Background(backgroundImage));

        stage.setScene(new Scene(root, 600, 400));
        stage.setTitle("Background Test");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
