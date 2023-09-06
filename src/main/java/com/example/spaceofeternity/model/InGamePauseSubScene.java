package com.example.spaceofeternity.model;

import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.io.File;

public class InGamePauseSubScene extends SubScene {
    private final String MAGENTA_BACKGROUND_IMAGE = "src/main/resources/resources/mainmenu/magenta_panel.png";

    private static final int WIDTH = 600 / 2 + 100;
    private static final int HEIGHT = 800 / 2 + 100;

    private boolean isHidden;

    public InGamePauseSubScene() {
        super(new AnchorPane(), WIDTH, HEIGHT);
        prefWidth(WIDTH);
        prefHeight(HEIGHT);

        BackgroundImage image = new BackgroundImage(new Image(new File(MAGENTA_BACKGROUND_IMAGE).toURI().toString(),
                WIDTH, HEIGHT, false, true), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);

        AnchorPane root2 = (AnchorPane) this.getRoot();
        root2.setBackground(new Background(image));

        isHidden = true;
        setOpacity(0);
        setLayoutX(WIDTH / 4);
        setLayoutY(HEIGHT / 4 + 40);
    }

    public void moveSubScene() {
        if (isHidden) {
            setOpacity(100);
            isHidden = false;
        } else {
            setOpacity(0);
            isHidden = true;
        }
    }

    public AnchorPane getPane() { return (AnchorPane) this.getRoot(); }
}
