package com.example.spaceofeternity.model;

import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.text.Font;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GameButton extends Button {
    private final String FONT_PATH = "src/main/resources/resources/font/minecraft.ttf";
    private final String GREEN_BUTTON_FREE_IMAGE = "src/main/resources/resources/mainmenu/green_button.png";
    private final String GREEN_BUTTON_PRESSED_IMAGE = "src/main/resources/resources/mainmenu/green_button_pressed.png";
    private final String RED_BUTTON_FREE_IMAGE = "src/main/resources/resources/mainmenu/red_button.png";
    private final String RED_BUTTON_PRESSED_IMAGE = "src/main/resources/resources/mainmenu/red_button_pressed.png";
    private final String BLUE_BUTTON_FREE_IMAGE = "src/main/resources/resources/mainmenu/blue_button.png";
    private final String BLUE_BUTTON_PRESSED_IMAGE = "src/main/resources/resources/mainmenu/blue_button_pressed.png";
    private final String YELLOW_BUTTON_FREE_IMAGE = "src/main/resources/resources/mainmenu/yellow_button.png";
    private final String YELLOW_BUTTON_PRESSED_IMAGE = "src/main/resources/resources/mainmenu/yellow_button_pressed.png";
    private final String MAGENTA_BUTTON_FREE_IMAGE = "src/main/resources/resources/mainmenu/magenta_button.png";
    private final String MAGENTA_BUTTON_PRESSED_IMAGE = "src/main/resources/resources/mainmenu/magenta_button_pressed.png";
    private final String GREY_BUTTON_FREE_IMAGE = "src/main/resources/resources/mainmenu/grey_button.png";
    private final String GREY_BUTTON_PRESSED_IMAGE = "src/main/resources/resources/mainmenu/grey_button_pressed.png";

    private static int WIDTH = 190;
    private static int HEIGHT = 49;

    public GameButton(String text) {
        setText(text);
        setButtonFont();
        setPrefWidth(WIDTH);
        setPrefHeight(HEIGHT);
        ButtonTypes buttonType = ButtonTypes.GREEN;
        BackgroundImage backgroundImage = new BackgroundImage(new Image(new File(GREEN_BUTTON_FREE_IMAGE).toURI().toString(),
                240, 39,false, true), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);
        setBackground(new Background(backgroundImage));
        initializeButtonListeners(buttonType);
    }

    public GameButton(String text, ButtonTypes buttonType) {
        setText(text);
        setButtonFont();
        setPrefWidth(WIDTH);
        setPrefHeight(HEIGHT);
        Image backgroundImage = new Image(new File(GREEN_BUTTON_FREE_IMAGE).toURI().toString(), WIDTH, HEIGHT, false, true);
        switch (buttonType) {
            case RED -> backgroundImage = new Image(new File(RED_BUTTON_FREE_IMAGE).toURI().toString(), WIDTH, HEIGHT, false, true);
            case BLUE -> backgroundImage = new Image(new File(BLUE_BUTTON_FREE_IMAGE).toURI().toString(), WIDTH, HEIGHT, false, true);
            case YELLOW -> backgroundImage = new Image(new File(YELLOW_BUTTON_FREE_IMAGE).toURI().toString(), WIDTH, HEIGHT, false, true);
            case MAGENTA -> backgroundImage = new Image(new File(MAGENTA_BUTTON_FREE_IMAGE).toURI().toString(), WIDTH, HEIGHT, false, true);
            case GREY -> backgroundImage = new Image(new File(GREY_BUTTON_FREE_IMAGE).toURI().toString(), WIDTH, HEIGHT, false, true);
        }
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
        setBackground(new Background(background));
        initializeButtonListeners(buttonType);
    }

    public void setButtonFont() {
        try {
            setFont(Font.loadFont(new FileInputStream(FONT_PATH), 23));
        } catch (FileNotFoundException e) {
            setFont(Font.font("Verdana", 23));
        }
    }

    public void setButtonPressedStyle(ButtonTypes buttonType) {
        Image backgroundImage = new Image(new File(GREEN_BUTTON_PRESSED_IMAGE).toURI().toString(), WIDTH, HEIGHT - 4, false, true);
        switch (buttonType) {
            case RED -> backgroundImage = new Image(new File(RED_BUTTON_PRESSED_IMAGE).toURI().toString(), WIDTH, HEIGHT - 4, false, true);
            case BLUE -> backgroundImage = new Image(new File(BLUE_BUTTON_PRESSED_IMAGE).toURI().toString(), WIDTH, HEIGHT - 4, false, true);
            case YELLOW -> backgroundImage = new Image(new File(YELLOW_BUTTON_PRESSED_IMAGE).toURI().toString(), WIDTH, HEIGHT - 4, false, true);
            case MAGENTA -> backgroundImage = new Image(new File(MAGENTA_BUTTON_PRESSED_IMAGE).toURI().toString(), WIDTH, HEIGHT - 4, false, true);
            case GREY -> backgroundImage = new Image(new File(GREY_BUTTON_PRESSED_IMAGE).toURI().toString(), WIDTH, HEIGHT - 4, false, true);
        }
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
        setBackground(new Background(background));
        setPrefHeight(45);
        setLayoutY(getLayoutY() + 4);
    }

    public void setButtonReleasedStyle(ButtonTypes buttonType) {
        Image backgroundImage = new Image(new File(GREEN_BUTTON_FREE_IMAGE).toURI().toString(), WIDTH, HEIGHT, false, true);
        switch (buttonType) {
            case RED -> backgroundImage = new Image(new File(RED_BUTTON_FREE_IMAGE).toURI().toString(), WIDTH, HEIGHT, false, true);
            case BLUE -> backgroundImage = new Image(new File(BLUE_BUTTON_FREE_IMAGE).toURI().toString(), WIDTH, HEIGHT, false, true);
            case YELLOW -> backgroundImage = new Image(new File(YELLOW_BUTTON_FREE_IMAGE).toURI().toString(), WIDTH, HEIGHT, false, true);
            case MAGENTA -> backgroundImage = new Image(new File(MAGENTA_BUTTON_FREE_IMAGE).toURI().toString(), WIDTH, HEIGHT, false, true);
            case GREY -> backgroundImage = new Image(new File(GREY_BUTTON_FREE_IMAGE).toURI().toString(), WIDTH, HEIGHT, false, true);
        }
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
        setBackground(new Background(background));
        setPrefHeight(49);
        setLayoutY(getLayoutY() - 4);
    }

    public void initializeButtonListeners(ButtonTypes buttonType) {
        setOnMousePressed(mouseEvent -> {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                setButtonPressedStyle(buttonType);
            }
        });

        setOnMouseReleased(mouseEvent -> {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                setButtonReleasedStyle(buttonType);
            }
        });

        setOnMouseEntered(mouseEvent -> {
            setEffect(new DropShadow());
        });

        setOnMouseExited(mouseEvent -> {
            setEffect(null);
        });
    }
}
