package com.example.spaceofeternity.model;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class HelpLabel extends Label {
    private final static String FONT_PATH = "src/main/resources/resources/font/minecraft.ttf";

    public HelpLabel(String text) {
        setPrefWidth(560);
        setPrefHeight(560);
        setWrapText(true);
        setAlignment(Pos.TOP_CENTER);
        setPadding(new Insets(10, 10, 10, 10));
        setText(text);
        setLabelFont();
    }

    private void setLabelFont() {
        try {
            setFont(Font.loadFont(new FileInputStream(new File(FONT_PATH)), 18));
        } catch (FileNotFoundException e) {
            setFont(Font.font("Verdana", 18));
        }
    }
}
