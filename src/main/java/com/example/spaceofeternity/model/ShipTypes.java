package com.example.spaceofeternity.model;

public enum ShipTypes {
    ANCHOR("src/main/resources/resources/shipchooser/anchorShip.png"),
    AXE("src/main/resources/resources/shipchooser/axeShip.png"),
    CRAB("src/main/resources/resources/shipchooser/crabShip.png"),
    DODGER("src/main/resources/resources/shipchooser/dodgerShip.png"),
    HAMMER("src/main/resources/resources/shipchooser/hammerShip.png"),
    MINIC("src/main/resources/resources/shipchooser/minicShip.png"),
    PLANER("src/main/resources/resources/shipchooser/planerShip.png"),
    SPEEDSTER("src/main/resources/resources/shipchooser/speedsterShip.png"),
    WHEEL("src/main/resources/resources/shipchooser/wheelShip.png");

    private String urlShip;

    private ShipTypes(String urlShip) { this.urlShip = urlShip; }

    public String getUrl() { return this.urlShip; }

    public String getName() { return this.name(); }
}
