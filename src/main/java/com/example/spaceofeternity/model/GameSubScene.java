package com.example.spaceofeternity.model;

import javafx.animation.TranslateTransition;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.io.File;

public class GameSubScene extends SubScene {
    private final String RED_BACKGROUND_IMAGE = "src/main/resources/resources/mainmenu/red_panel.png";
    private final String BLUE_BACKGROUND_IMAGE = "src/main/resources/resources/mainmenu/blue_panel.png";
    private final String GREEN_BACKGROUND_IMAGE = "src/main/resources/resources/mainmenu/green_panel.png";
    private final String YELLOW_BACKGROUND_IMAGE = "src/main/resources/resources/mainmenu/yellow_panel.png";

    private static final int  WIDTH = 600;
    private static final int HEIGHT = 540;

    private boolean isHidden;

    public GameSubScene() {
        super(new AnchorPane(), WIDTH, HEIGHT);
        prefWidth(WIDTH);
        prefHeight(HEIGHT);


        BackgroundImage image = new BackgroundImage(new Image(new File(YELLOW_BACKGROUND_IMAGE).toURI().toString(),
                WIDTH, HEIGHT, false, true), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);

        AnchorPane root2 = (AnchorPane) this.getRoot();
        root2.setBackground(new Background(image));

        isHidden = true;

        setLayoutX(-WIDTH - 25);
        setLayoutY(160);
    }

    public GameSubScene(ButtonTypes buttonType) {
        super(new AnchorPane(), WIDTH, HEIGHT);
        prefWidth(WIDTH);
        prefHeight(HEIGHT);

        Image img = new Image(new File(YELLOW_BACKGROUND_IMAGE).toURI().toString(), WIDTH, HEIGHT, false, true);;
        switch (buttonType) {
            case RED -> img = new Image(new File(RED_BACKGROUND_IMAGE).toURI().toString(), WIDTH, HEIGHT, false, true);
            case BLUE -> img = new Image(new File(BLUE_BACKGROUND_IMAGE).toURI().toString(), WIDTH, HEIGHT, false, true);
            case GREEN -> img = new Image(new File(GREEN_BACKGROUND_IMAGE).toURI().toString(), WIDTH, HEIGHT, false, true);
        }

        BackgroundImage image = new BackgroundImage(img, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
                BackgroundPosition.DEFAULT, null);

        AnchorPane root2 = (AnchorPane) this.getRoot();
        root2.setBackground(new Background(image));

        isHidden = true;

        setLayoutX(-WIDTH - 25);
        setLayoutY(160);
    }

    public void moveSubScene() {
        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.seconds(0.4));
        transition.setNode(this);
        if (isHidden) {
            transition.setToX(670);
            isHidden = false;
        } else {
            transition.setToX(0);
            isHidden = true;
        }
        transition.play();
    }

    public AnchorPane getPane() { return (AnchorPane) this.getRoot(); }
}
