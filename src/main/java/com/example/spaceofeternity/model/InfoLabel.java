package com.example.spaceofeternity.model;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.text.Font;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class InfoLabel extends Label {
    private final static String FONT_PATH = "src/main/resources/resources/font/minecraft.ttf";
    private final static String RED_SMALL_PANEL = "src/main/resources/resources/mainmenu/red_small_panel.png";
    private final static String MAGENTA_SMALL_PANEL = "src/main/resources/resources/mainmenu/magenta_small_panel.png";
    private final static String GREEN_SMALL_PANEL = "src/main/resources/resources/mainmenu/green_small_panel.png";
    private final static String BLUE_SMALL_PANEL = "src/main/resources/resources/mainmenu/blue_small_panel.png";

    private int WIDTH = 380;
    private int HEIGHT = 49;

    private int FONT_SIZE = 23;

    public InfoLabel(String text) {
        setPrefWidth(WIDTH);
        setPrefHeight(HEIGHT);
        setText(text);
        setWrapText(true);
        setLabelFont();
        setAlignment(Pos.CENTER);

        BackgroundImage backgroundImage = new BackgroundImage(new Image(new File(RED_SMALL_PANEL).toURI().toString(),
                WIDTH, HEIGHT,false, true), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);
        setBackground(new Background(backgroundImage));
    }

    public InfoLabel(String text, ButtonTypes buttonTypes) {
        setPrefWidth(WIDTH);
        setPrefHeight(HEIGHT);
        setText(text);
        setWrapText(true);
        setLabelFont();
        setAlignment(Pos.CENTER);

        Image img = new Image(new File(RED_SMALL_PANEL).toURI().toString(), WIDTH, HEIGHT, false, true);
        switch (buttonTypes) {
            case MAGENTA -> img = new Image(new File(MAGENTA_SMALL_PANEL).toURI().toString(), WIDTH, HEIGHT, false, true);
            case GREEN -> img = new Image(new File(GREEN_SMALL_PANEL).toURI().toString(), WIDTH, HEIGHT, false, true);
            case BLUE -> img = new Image(new File(BLUE_SMALL_PANEL).toURI().toString(), WIDTH, HEIGHT, false, true);
        }
        BackgroundImage backgroundImage = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT, null);
        setBackground(new Background(backgroundImage));
    }

    public InfoLabel(String text, ButtonTypes buttonTypes, int width, int height) {
        WIDTH = width;
        HEIGHT = height;
        setPrefWidth(WIDTH);
        setPrefHeight(HEIGHT);
        setText(text);
        setWrapText(true);
        setLabelFont();
        setAlignment(Pos.CENTER);

        Image img = new Image(new File(RED_SMALL_PANEL).toURI().toString(), WIDTH, HEIGHT, false, true);
        switch (buttonTypes) {
            case MAGENTA -> img = new Image(new File(MAGENTA_SMALL_PANEL).toURI().toString(), WIDTH, HEIGHT, false, true);
            case GREEN -> img = new Image(new File(GREEN_SMALL_PANEL).toURI().toString(), WIDTH, HEIGHT, false, true);
            case BLUE -> img = new Image(new File(BLUE_SMALL_PANEL).toURI().toString(), WIDTH, HEIGHT, false, true);
        }
        BackgroundImage backgroundImage = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT, null);
        setBackground(new Background(backgroundImage));
    }

    public InfoLabel(String text, ButtonTypes buttonTypes, int width, int height, int fontSize) {
        WIDTH = width;
        HEIGHT = height;
        FONT_SIZE = fontSize;
        setPrefWidth(WIDTH);
        setPrefHeight(HEIGHT);
        setText(text);
        setWrapText(true);
        setLabelFont();
        setAlignment(Pos.CENTER);

        Image img = new Image(new File(RED_SMALL_PANEL).toURI().toString(), WIDTH, HEIGHT, false, true);
        switch (buttonTypes) {
            case MAGENTA -> img = new Image(new File(MAGENTA_SMALL_PANEL).toURI().toString(), WIDTH, HEIGHT, false, true);
            case GREEN -> img = new Image(new File(GREEN_SMALL_PANEL).toURI().toString(), WIDTH, HEIGHT, false, true);
            case BLUE -> img = new Image(new File(BLUE_SMALL_PANEL).toURI().toString(), WIDTH, HEIGHT, false, true);
        }
        BackgroundImage backgroundImage = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT, null);
        setBackground(new Background(backgroundImage));
    }

    private void setLabelFont() {
        try {
            setFont(Font.loadFont(new FileInputStream(FONT_PATH), FONT_SIZE));
        } catch (FileNotFoundException e) {
            setFont(Font.font("Verdana", FONT_SIZE));
        }
    }
}
