package com.example.spaceofeternity.model;

import javafx.geometry.Insets;
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

public class SmallScoreInfoLabel extends Label {
    private String FONT_PATH = "src/main/resources/resources/font/minecraft.ttf";
    private String MAGENTA_INFO_LABEL_IMAGE = "src/main/resources/resources/game/magenta_info_label.png";

    public SmallScoreInfoLabel(String text) {
        setPrefWidth(186);
        setPrefHeight(50);
        BackgroundImage backgroundImage = new BackgroundImage(new Image(new File(MAGENTA_INFO_LABEL_IMAGE).toURI().toString(),
                186, 50, false, true), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);
        setBackground(new Background(backgroundImage));
        setAlignment(Pos.CENTER_LEFT);
        setPadding(new Insets(10, 10, 10, 10));
        setText(text);
        setLabelFont();
    }

    private void setLabelFont() {
        try {
            setFont(Font.loadFont(new FileInputStream(new File(FONT_PATH)), 15));
        } catch (FileNotFoundException e) {
            setFont(Font.font("Verdana", 15));
        }
    }
}
