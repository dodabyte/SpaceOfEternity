package com.example.spaceofeternity.model;

import javafx.scene.control.ProgressBar;

public class SmallHealthInfoBar extends ProgressBar {
    private final static String HEALTH_BAR_STYLE = "-fx-accent: darkred; -fx-text-box-border: purple;";

    public SmallHealthInfoBar() {
        setPrefWidth(185);
        setPrefHeight(50);
        setStyle(HEALTH_BAR_STYLE);
        setProgress(1);
    }
}
