package com.example.spaceofeternity.model;

public enum MeteorsType {
    BIG_BROWN_1("src/main/resources/resources/game/meteor_brown_big1.png", "BIG", "BROWN"),
    BIG_BROWN_2("src/main/resources/resources/game/meteor_brown_big3.png", "BIG", "BROWN"),
    BIG_BROWN_3("src/main/resources/resources/game/meteor_brown_big4.png", "BIG", "BROWN"),
    MEDIUM_BROWN_1("src/main/resources/resources/game/meteor_brown_medium1.png", "MEDIUM", "BROWN"),
    MEDIUM_BROWN_2("src/main/resources/resources/game/meteor_brown_medium2.png", "MEDIUM", "BROWN"),
    SMALL_BROWN_1("src/main/resources/resources/game/meteor_brown_small1.png", "SMALL", "BROWN"),
    BIG_GREY_1("src/main/resources/resources/game/meteor_grey_big1.png", "BIG", "GREY"),
    BIG_GREY_2("src/main/resources/resources/game/meteor_grey_big3.png", "BIG", "GREY"),
    BIG_GREY_3("src/main/resources/resources/game/meteor_grey_big4.png", "BIG", "GREY"),
    MEDIUM_GREY_1("src/main/resources/resources/game/meteor_grey_medium1.png", "MEDIUM", "GREY"),
    MEDIUM_GREY_2("src/main/resources/resources/game/meteor_grey_medium2.png", "MEDIUM", "GREY"),
    SMALL_GREY_1("src/main/resources/resources/game/meteor_grey_small1.png", "SMALL", "GREY");

    private String urlMeteor;
    private String size;
    private String color;

    private MeteorsType(String urlMeteor, String size, String color) {
        this.urlMeteor = urlMeteor;
        this.size = size;
        this.color = color;
    }

    public String getUrl() { return this.urlMeteor; }

    public String getSize() { return this.size; }

    public String getColor() { return this.color; }

    public String getName() { return this.name(); }
}
