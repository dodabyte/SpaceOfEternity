package com.example.spaceofeternity.model;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.text.Font;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class NickNameTextField extends TextField {
    private final static String FONT_PATH = "src/main/resources/resources/font/minecraft.ttf";
    private final static String RED_SMALL_PANEL = "src/main/resources/resources/mainmenu/grey_small_panel.png";

    private int WIDTH = 320;
    private int HEIGHT = 49;

    private static final int LIMIT = 16;

    public NickNameTextField(String text) {
        setPrefWidth(WIDTH);
        setPrefHeight(HEIGHT);
        setPromptText(text);
        setLabelFont();
        setAlignment(Pos.CENTER);
        lengthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                if (newValue.intValue() > oldValue.intValue()) {
                    if (getText().length() >= LIMIT) {
                        setText(getText().substring(0, LIMIT));
                    }
                }
            }
        });

        BackgroundImage backgroundImage = new BackgroundImage(new Image(new File(RED_SMALL_PANEL).toURI().toString(),
                WIDTH, HEIGHT,false, true), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);
        setBackground(new Background(backgroundImage));
    }

    private void setLabelFont() {
        try {
            setFont(Font.loadFont(new FileInputStream(FONT_PATH), 23));
        } catch (FileNotFoundException e) {
            setFont(Font.font("Verdana", 23));
        }
    }
}
