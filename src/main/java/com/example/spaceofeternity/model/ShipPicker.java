package com.example.spaceofeternity.model;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ShipPicker extends VBox {
    private ImageView circleImage;
    private ImageView shipImage;
    private Text text;

    private final static String FONT_PATH = "src/main/resources/resources/font/minecraft.ttf";
    private String circleNotChoosen = "src/main/resources/resources/shipchooser/grey_circle.png";
    private String circleChoosen = "src/main/resources/resources/shipchooser/circle_choosen.png";

    private ShipTypes ship;

    private boolean isCircleChoosen;

    public ShipPicker(ShipTypes ship) {
        circleImage = new ImageView(new File(circleNotChoosen).toURI().toString());
        circleImage.setFitWidth(20);
        circleImage.setFitHeight(20);
        shipImage = new ImageView(new File(ship.getUrl()).toURI().toString());
        shipImage.setFitWidth(60);
        shipImage.setFitHeight(60);
        text = new Text(ship.getName());
        setLabelFont();
        this.ship = ship;
        isCircleChoosen = false;
        this.setAlignment(Pos.CENTER);
        this.setSpacing(10);
        this.getChildren().add(circleImage);
        this.getChildren().add(shipImage);
        this.getChildren().add(text);
    }

    public ShipTypes getShip() { return ship; }

    public boolean getIsCircleChoosen() { return isCircleChoosen; }

    public void setIsCircleChoosen(boolean isCircleChoosen) {
        this.isCircleChoosen = isCircleChoosen;
        String imageToSet = this.isCircleChoosen ? circleChoosen : circleNotChoosen;
        circleImage.setImage(new Image(new File(imageToSet).toURI().toString()));
    }

    private void setLabelFont() {
        try {
            text.setFont(Font.loadFont(new FileInputStream(FONT_PATH), 14));
        } catch (FileNotFoundException e) {
            text.setFont(Font.font("Verdana", 14));
        }
    }
}
