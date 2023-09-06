package com.example.spaceofeternity.application;

import com.example.spaceofeternity.view.ViewManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        try {
            ViewManager manager = new ViewManager();
            primaryStage = manager.getMainStage();
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) { launch(); }
}