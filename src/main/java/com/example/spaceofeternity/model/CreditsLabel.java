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

public class CreditsLabel extends Label {
    public final static String FONT_PATH = "src/main/resources/resources/font/minecraft.ttf";

    public final static String BACKGROUND_IMAGE = "src/main/resources/resources/mainmenu/yellow_small_panel.png";

    public CreditsLabel(String text) {
        setPrefWidth(240);
        setPrefHeight(39);
        setText(text);
        setWrapText(true);
        setLabelFont();
        setAlignment(Pos.CENTER);
        BackgroundImage backgroundImage = new BackgroundImage(new Image(new File(BACKGROUND_IMAGE).toURI().toString(),
                240, 39,false, true), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);
        setBackground(new Background(backgroundImage));
    }

    private void setLabelFont() {
        try {
            setFont(Font.loadFont(new FileInputStream(FONT_PATH), 20));
        } catch (FileNotFoundException e) {
            setFont(Font.font("Verdana", 20));
        }
    }
}
