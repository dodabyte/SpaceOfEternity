package com.example.spaceofeternity.view;

import com.example.spaceofeternity.data.Client;
import com.example.spaceofeternity.data.Database;
import com.example.spaceofeternity.model.*;
import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class GameViewManager extends ViewManager {
    private AnchorPane gamePane;
    private Scene gameScene;
    private Stage gameStage;
    MediaPlayer gameMusic;
    AudioClip buttonClickSound;
    AudioClip pauseGameSound;
    AudioClip giveUpStarSound;
    AudioClip explosionCrunchSound;

    private static final int GAME_WIDTH = 600;
    private static final int GAME_HEIGHT = 800;

    private Database database;
    private Client client;

    private final static String NAME_GAME = "Space of Eternity";
    private final static String PATH_ICON_IMAGE = "src/main/resources/resources/shipchooser/planerShip.png";
    private final static String CURSOR_IMAGE = "src/main/resources/resources/mainmenu/cursor.png";
    private final static String GAME_MUSIC_PATH = "src/main/resources/resources/sounds/game_music.mp3";
    private final static String BUTTON_CLICK_SOUND_PATH = "src/main/resources/resources/sounds/select.mp3";
    private final static String PAUSE_GAME_SOUND_PATH = "src/main/resources/resources/sounds/pause_game.wav";
    private final static String GIVE_UP_STAR_SOUND_PATH = "src/main/resources/resources/sounds/give_up_star.wav";
    private final static String EXPLOSION_CRUNCH_SOUND_PATH = "src/main/resources/resources/sounds/explosion_crunch.wav";

    private static final int GLOBAL_COUNT_BIG_METEORS = 5;
    private static final int GLOBAL_COUNT_MEDIUM_METEORS = 10;

    private Stage menuStage;
    private ImageView ship;
    private int speedMoveShip;

    private InGamePauseSubScene pauseSubScene;

    private boolean isLeftKeyPressed;
    private boolean isRightKeyPressed;
    private boolean isPause;
    private int angle;
    private AnimationTimer gameTimer;
    private int time;

    private AnchorPane anchorPane1;
    private AnchorPane anchorPane2;
    private final static String BACKGROUND1_IMAGE = "src/main/resources/resources/game/gameMemeBackground1.jpg";
    private final static String BACKGROUND2_IMAGE = "src/main/resources/resources/game/gameMemeBackground2.jpg";

    private MeteorsType meteorsType;
    private ArrayList<ImageView> bigBrownMeteors;
    private ArrayList<ImageView> bigGreyMeteors;
    private ArrayList<ImageView> mediumBrownMeteors;
    private ArrayList<ImageView> mediumGreyMeteors;
    Random randomPositionGenerator;
    ArrayList<ImageView> brokenMeteors;
    private int speedMoveMeteors;

    private SmallScoreInfoLabel scoresLabel;
    private SmallHealthInfoBar healthBar;
    private SimpleLabel healthLabel;
    private ImageView star;
    private int speedMoveStar;
    private int playerLife;
    private int points;
    private String nickName;
    private final static String GOLD_STAR_IMAGE = "src/main/resources/resources/game/gold_star.png";

    private final static int STAR_RADIUS = 12;
    private final static int SHIP_RADIUS = 27;
    private final static int BIG_METEOR_RADIUS = 25;
    private final static int MEDIUM_METEOR_RADIUS = 20;

    public GameViewManager(String nickName) {
        this.nickName = nickName;
        initializeStage();
        createKeyListeners();
        database = new Database();
        bigBrownMeteors = new ArrayList<>();
        bigGreyMeteors = new ArrayList<>();
        mediumBrownMeteors = new ArrayList<>();
        mediumGreyMeteors = new ArrayList<>();
        randomPositionGenerator = new Random();
    }

    private void initializeStage() {
        gamePane = new AnchorPane();
        gameScene = new Scene(gamePane, GAME_WIDTH, GAME_HEIGHT);
        gameStage = new Stage();
        gameStage.setScene(gameScene);
        createSounds();
        setStageProperties();
    }

    private void setStageProperties() {
        gameStage.getIcons().add(new Image(new File(PATH_ICON_IMAGE).toURI().toString()));
        gameStage.setTitle(NAME_GAME);
        gameStage.getScene().setCursor(new ImageCursor(new Image(new File(CURSOR_IMAGE).toURI().toString())));
    }

    private void createKeyListeners() {
        gameScene.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.A || keyEvent.getCode() == KeyCode.LEFT) {
                isLeftKeyPressed = true;
            } else if (keyEvent.getCode() == KeyCode.D || keyEvent.getCode() == KeyCode.RIGHT)  {
                isRightKeyPressed = true;
            } else if (keyEvent.getCode() == KeyCode.ESCAPE) {
                pauseGame();
            }
        });

        gameScene.setOnKeyReleased(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.A || keyEvent.getCode() == KeyCode.LEFT) {
                isLeftKeyPressed = false;
            } else if (keyEvent.getCode() == KeyCode.D || keyEvent.getCode() == KeyCode.RIGHT)  {
                isRightKeyPressed = false;
            }
        });
    }

    public void createNewGame(Stage menuStage, ShipTypes choosenShip) {
        this.menuStage = menuStage;
        this.menuStage.hide();
        createBackground();
        createShip(choosenShip);
        firstCreateGameElements();
        createGameLoop();
        createEscapePauseSubScene();
        gameStage.show();
    }

    private void createEscapePauseSubScene() {
        isPause = false;
        pauseSubScene = new InGamePauseSubScene();
        InfoLabel quitLabel = new InfoLabel("QUIT THE GAME?", ButtonTypes.MAGENTA, 300, 49);
        quitLabel.setLayoutX(52);
        quitLabel.setLayoutY(120);
        quitLabel.setPrefWidth(300);
        pauseSubScene.getPane().getChildren().addAll(createButtonYesInPauseSubScene(), createButtonNoInPauseSubScene(), quitLabel);
        gamePane.getChildren().add(pauseSubScene);
    }

    private void pauseGame() {
        if (isPause) {
            buttonClickSound.play();
            isPause = false;
            pauseSubScene.moveSubScene();
            gameMusic.play();
        } else {
            pauseGameSound.play();
            isPause = true;
            pauseSubScene.moveSubScene();
            pauseSubScene.toFront();
            gameMusic.pause();
        }
    }

    private GameButton createButtonYesInPauseSubScene() {
        GameButton buttonYes = new GameButton("YES", ButtonTypes.GREEN);
        buttonYes.setLayoutX(106);
        buttonYes.setLayoutY(270);

        buttonYes.setOnAction(actionEvent -> {
            buttonClickSound.play();
            gameStage.close();
            gameTimer.stop();
            menuStage.show();
        });

        return buttonYes;
    }

    private GameButton createButtonNoInPauseSubScene() {
        GameButton buttonNo = new GameButton("NO", ButtonTypes.RED);
        buttonNo.setLayoutX(106);
        buttonNo.setLayoutY(340);

        buttonNo.setOnAction(actionEvent -> {
            buttonClickSound.play();
            isPause = false;
            pauseSubScene.moveSubScene();
            gameMusic.play();
        });

        return buttonNo;
    }

    private void firstCreateGameElements() {
        playerLife = 100;
        star = new ImageView(new File(GOLD_STAR_IMAGE).toURI().toString());
        star.setFitWidth(star.getImage().getWidth() / 11);
        star.setFitHeight(star.getImage().getHeight() / 11);
        setNewElementPosition(star);
        speedMoveStar = 5;
        scoresLabel = new SmallScoreInfoLabel("POINTS: 00");
        scoresLabel.setLayoutX(400);
        scoresLabel.setLayoutY(20);
        healthBar = new SmallHealthInfoBar();
        healthBar.setLayoutX(400);
        healthBar.setLayoutY(65);
        healthLabel = new SimpleLabel("HEALTH: " + playerLife + "/100");
        healthLabel.setAlignment(Pos.CENTER_LEFT);
        healthLabel.setLayoutX(400);
        healthLabel.setLayoutY(67);
        gamePane.getChildren().addAll(star, scoresLabel, healthBar, healthLabel);

        for (int i = 0; i < GLOBAL_COUNT_BIG_METEORS; i++) {
            createBigMeteor();
        }

        for (int i = 0; i < GLOBAL_COUNT_MEDIUM_METEORS; i++) {
            createMediumMeteor();
        }

        brokenMeteors = new ArrayList<>();
        speedMoveMeteors = 7;
    }

    private void createBigMeteor() {
        meteorsType = randomBigTypeMeteor();
        ImageView image = new ImageView(new File(meteorsType.getUrl()).toURI().toString());
        if (meteorsType.getColor().equals("BROWN")) {
            bigBrownMeteors.add(image);
            setNewElementPosition(bigBrownMeteors.get(bigBrownMeteors.size() - 1));
            gamePane.getChildren().add(bigBrownMeteors.get(bigBrownMeteors.size() - 1));
        } else if (meteorsType.getColor().equals("GREY")) {
            bigGreyMeteors.add(image);
            setNewElementPosition(bigGreyMeteors.get(bigGreyMeteors.size() - 1));
            gamePane.getChildren().add(bigGreyMeteors.get(bigGreyMeteors.size() - 1));
        }
        toFrontUI();
    }

    private void createMediumMeteor() {
        meteorsType = randomMediumTypeMeteor();
        ImageView image = new ImageView(new File(meteorsType.getUrl()).toURI().toString());
        if (meteorsType.getColor().equals("BROWN")) {
            mediumBrownMeteors.add(image);
            setNewElementPosition(mediumBrownMeteors.get(mediumBrownMeteors.size() - 1));
            gamePane.getChildren().add(mediumBrownMeteors.get(mediumBrownMeteors.size() - 1));
        } else if (meteorsType.getColor().equals("GREY")) {
            mediumGreyMeteors.add(image);
            setNewElementPosition(mediumGreyMeteors.get(mediumGreyMeteors.size() - 1));
            gamePane.getChildren().add(mediumGreyMeteors.get(mediumGreyMeteors.size() - 1));
        }
        toFrontUI();
    }

    private void setNewElementPosition(ImageView image) {
        image.setLayoutX(randomPositionGenerator.nextInt(512));
        image.setLayoutY(-(randomPositionGenerator.nextInt(3200) + 600));
    }

    private MeteorsType randomBigTypeMeteor() {
        int randColor = (int) (Math.random() * 2);
        int randType = (int) (Math.random() * 3);
        if (randColor == 0) {
            return switch (randType) {
                case 1 -> MeteorsType.BIG_BROWN_2;
                case 2 -> MeteorsType.BIG_BROWN_3;
                default -> MeteorsType.BIG_BROWN_1;
            };
        } else {
            return switch (randType) {
                case 1 -> MeteorsType.BIG_GREY_2;
                case 2 -> MeteorsType.BIG_GREY_3;
                default -> MeteorsType.BIG_GREY_1;
            };
        }
    }

    private MeteorsType randomMediumTypeMeteor() {
        int randColor = (int) (Math.random() * 2);
        int randType = (int) (Math.random() * 4);
        if (randColor == 0) {
            return switch (randType) {
                case 1 -> MeteorsType.MEDIUM_BROWN_2;
                default -> MeteorsType.MEDIUM_BROWN_1;
            };
        } else {
            return switch (randType) {
                case 1 -> MeteorsType.MEDIUM_GREY_2;
                default -> MeteorsType.MEDIUM_GREY_1;
            };
        }
    }

    private void moveGameElements() {
        star.setLayoutY(star.getLayoutY() + speedMoveStar);

        for (int i = 0; i < bigBrownMeteors.size(); i++) {
            bigBrownMeteors.get(i).setLayoutY(bigBrownMeteors.get(i).getLayoutY() + speedMoveMeteors);
            bigBrownMeteors.get(i).setRotate(bigBrownMeteors.get(i).getRotate() + 4);
        }

        for (int i = 0; i < mediumBrownMeteors.size(); i++) {
            mediumBrownMeteors.get(i).setLayoutY(mediumBrownMeteors.get(i).getLayoutY() + speedMoveMeteors);
            mediumBrownMeteors.get(i).setRotate(mediumBrownMeteors.get(i).getRotate() + 4);
        }

        for (int i = 0; i < bigGreyMeteors.size(); i++) {
            bigGreyMeteors.get(i).setLayoutY(bigGreyMeteors.get(i).getLayoutY() + speedMoveMeteors);
            bigGreyMeteors.get(i).setRotate(bigGreyMeteors.get(i).getRotate() + 4);
        }

        for (int i = 0; i < mediumGreyMeteors.size(); i++) {
            mediumGreyMeteors.get(i).setLayoutY(mediumGreyMeteors.get(i).getLayoutY() + speedMoveMeteors);
            mediumGreyMeteors.get(i).setRotate(mediumGreyMeteors.get(i).getRotate() + 4);
        }

        for (int i = 0; i < brokenMeteors.size(); i++) {
            brokenMeteors.get(i).setLayoutY(brokenMeteors.get(i).getLayoutY() + 7);
            brokenMeteors.get(i).setRotate(brokenMeteors.get(i).getRotate() + 4);
        }
    }

    private void removingAndCreateNewGameElements() {
        if (star.getLayoutY() > 1200) { setNewElementPosition(star); }

        for (int i = 0; i < bigBrownMeteors.size(); i++) {
            if (bigBrownMeteors.get(i).getLayoutY() > 900) {
                gamePane.getChildren().remove(bigBrownMeteors.get(i));
                bigBrownMeteors.remove(i);
                createBigMeteor();
            }
        }
        for (int i = 0; i < bigGreyMeteors.size(); i++) {
            if (bigGreyMeteors.get(i).getLayoutY() > 900) {
                gamePane.getChildren().remove(bigGreyMeteors.get(i));
                bigGreyMeteors.remove(i);
                createBigMeteor();
            }
        }
        for (int i = 0; i < mediumBrownMeteors.size(); i++) {
            if (mediumBrownMeteors.get(i).getLayoutY() > 900) {
                gamePane.getChildren().remove(mediumBrownMeteors.get(i));
                mediumBrownMeteors.remove(i);
                createMediumMeteor();
            }
        }
        for (int i = 0; i < mediumGreyMeteors.size(); i++) {
            if (mediumGreyMeteors.get(i).getLayoutY() > 900) {
                gamePane.getChildren().remove(mediumGreyMeteors.get(i));
                mediumGreyMeteors.remove(i);
                createMediumMeteor();
            }
        }

        for (int i = 0; i < brokenMeteors.size(); i++) {
            if (brokenMeteors.get(i).getLayoutY() > 900) {
                gamePane.getChildren().remove(brokenMeteors.get(i));
                brokenMeteors.remove(i);
            }
        }
    }

    private void createShip(ShipTypes choosenShip) {
        ship = new ImageView(new File(choosenShip.getUrl()).toURI().toString());
        ship.setLayoutX(GAME_WIDTH / 2 - 30);
        ship.setLayoutY(GAME_HEIGHT - 120);
        ship.setFitWidth(ship.getImage().getWidth() / 3);
        ship.setFitHeight(ship.getImage().getHeight() / 3);
        gamePane.getChildren().add(ship);
        speedMoveShip = 3;
    }

    private void createGameLoop() {
        time = 0;
        gameTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                if (!isPause) {
                    time++;
                    changeSpeedGame();
                    moveBackground();
                    moveGameElements();
                    removingAndCreateNewGameElements();
                    checkCollide();
                    moveShip();
                }
            }
        };
        gameTimer.start();
    }

    private void moveShip() {
        if (isLeftKeyPressed && !isRightKeyPressed) {
            if (angle > -30) angle -= 5;
            ship.setRotate(angle);
            if (ship.getLayoutX() > 20) ship.setLayoutX(ship.getLayoutX() - speedMoveShip);
        }
        if (!isLeftKeyPressed && isRightKeyPressed) {
            if (angle < 30) angle += 5;
            ship.setRotate(angle);
            if (ship.getLayoutX() < GAME_WIDTH - ship.getFitWidth() - 20) ship.setLayoutX(ship.getLayoutX() + speedMoveShip);
        }
        if (!isLeftKeyPressed && !isRightKeyPressed) {
            if (angle < 0) angle += 5;
            else if (angle > 0) angle -=5;
            ship.setRotate(angle);
        }
        if (isLeftKeyPressed && isRightKeyPressed) {
            if (angle < 0) angle += 5;
            else if (angle > 0) angle -=5;
            ship.setRotate(angle);
        }
    }

    private void changeSpeedGame() {
        if (time % 1000 == 0 && speedMoveMeteors < 15) {
            speedMoveShip++;
            speedMoveMeteors++;
            speedMoveStar++;
        }
    }

    private void createBackground() {
        anchorPane1 = new AnchorPane();
        anchorPane2 = new AnchorPane();
        ImageView backgroundImage1 = new ImageView(new File(BACKGROUND1_IMAGE).toURI().toString());
        ImageView backgroundImage2 = new ImageView(new File(BACKGROUND2_IMAGE).toURI().toString());
        anchorPane1.getChildren().add(backgroundImage1);
        anchorPane2.getChildren().add(backgroundImage2);
        anchorPane2.setLayoutY(-800);
        gamePane.getChildren().addAll(anchorPane1, anchorPane2);
    }

    private void moveBackground() {
        anchorPane1.setLayoutY(anchorPane1.getLayoutY() + 0.5);
        anchorPane2.setLayoutY(anchorPane2.getLayoutY() + 0.5);

        if (anchorPane1.getLayoutY() >= 800) anchorPane1.setLayoutY(-800);
        if (anchorPane2.getLayoutY() >= 800) anchorPane2.setLayoutY(-800);
    }

    private void checkCollide() {
        if (SHIP_RADIUS + STAR_RADIUS > calculateDistance(ship.getLayoutX() + 49, star.getLayoutX() + 25,
                ship.getLayoutY() + 37, star.getLayoutY() + 25)) {
            giveUpStarSound.play();
            setNewElementPosition(star);
            points++;
            String textToSet = "POINTS: ";
            if (points < 10) { textToSet = textToSet + "0"; }
            scoresLabel.setText(textToSet + points);
        }

        for (int i = 0; i < bigBrownMeteors.size(); i++) {
            if (BIG_METEOR_RADIUS + SHIP_RADIUS > calculateDistance(ship.getLayoutX() + 47,
                    bigBrownMeteors.get(i).getLayoutX() + 70,
                    ship.getLayoutY() + 37, bigBrownMeteors.get(i).getLayoutY() + 46)) {
                explosionCrunchSound.play();
                for (int j = 0; j < 3; j++) {
                    ImageView image = new ImageView( new File(MeteorsType.MEDIUM_BROWN_1.getUrl()).toURI().toString());
                    brokenMeteors.add(image);
                    brokenMeteors.get(brokenMeteors.size() - 1).setLayoutX(bigBrownMeteors.get(i).getLayoutX() +
                            (int) (Math.random() * 100) - 50);
                    brokenMeteors.get(brokenMeteors.size() - 1).setLayoutY(bigBrownMeteors.get(i).getLayoutY() +
                            (int) (Math.random() * 50) - 25);
                    gamePane.getChildren().add(brokenMeteors.get(brokenMeteors.size() - 1));
                }
                gamePane.getChildren().remove(bigBrownMeteors.get(i));
                bigBrownMeteors.remove(i);
                removeLife(40);
                createBigMeteor();
            }
        }

        for (int i = 0; i < bigGreyMeteors.size(); i++) {
            if (BIG_METEOR_RADIUS + SHIP_RADIUS > calculateDistance(ship.getLayoutX() + 49, bigGreyMeteors.get(i).getLayoutX() + 70,
                    ship.getLayoutY() + 37, bigGreyMeteors.get(i).getLayoutY() + 46)) {
                explosionCrunchSound.play();
                for (int j = 0; j < 3; j++) {
                    ImageView image = new ImageView( new File(MeteorsType.MEDIUM_GREY_2.getUrl()).toURI().toString());
                    brokenMeteors.add(image);
                    brokenMeteors.get(brokenMeteors.size() - 1).setLayoutX(bigGreyMeteors.get(i).getLayoutX() +
                            (int) (Math.random() * 100) - 50);
                    brokenMeteors.get(brokenMeteors.size() - 1).setLayoutY(bigGreyMeteors.get(i).getLayoutY() +
                            (int) (Math.random() * 50) - 25);
                    gamePane.getChildren().add(brokenMeteors.get(brokenMeteors.size() - 1));
                }
                gamePane.getChildren().remove(bigGreyMeteors.get(i));
                bigGreyMeteors.remove(i);
                removeLife(40);
                createBigMeteor();
            }
        }

        for (int i = 0; i < mediumBrownMeteors.size(); i++) {
            if (MEDIUM_METEOR_RADIUS + SHIP_RADIUS > calculateDistance(ship.getLayoutX() + 49, mediumBrownMeteors.get(i).getLayoutX() + 20,
                    ship.getLayoutY() + 37, mediumBrownMeteors.get(i).getLayoutY() + 20)) {
                explosionCrunchSound.play();
                for (int j = 0; j < 3; j++) {
                    ImageView image = new ImageView( new File(MeteorsType.SMALL_BROWN_1.getUrl()).toURI().toString());
                    brokenMeteors.add(image);
                    brokenMeteors.get(brokenMeteors.size() - 1).setLayoutX(mediumBrownMeteors.get(i).getLayoutX() +
                            (int) (Math.random() * 100) - 50);
                    brokenMeteors.get(brokenMeteors.size() - 1).setLayoutY(mediumBrownMeteors.get(i).getLayoutY() +
                            (int) (Math.random() * 50) - 25);
                    gamePane.getChildren().add(brokenMeteors.get(brokenMeteors.size() - 1));
                }
                gamePane.getChildren().remove(mediumBrownMeteors.get(i));
                mediumBrownMeteors.remove(i);
                removeLife(25);
                createBigMeteor();
            }
        }

        for (int i = 0; i < mediumGreyMeteors.size(); i++) {
            if (MEDIUM_METEOR_RADIUS + SHIP_RADIUS > calculateDistance(ship.getLayoutX() + 49, mediumGreyMeteors.get(i).getLayoutX() + 20,
                    ship.getLayoutY() + 37, mediumGreyMeteors.get(i).getLayoutY() + 20)) {
                explosionCrunchSound.play();
                for (int j = 0; j < 3; j++) {
                    ImageView image = new ImageView( new File(MeteorsType.SMALL_GREY_1.getUrl()).toURI().toString());
                    brokenMeteors.add(image);
                    brokenMeteors.get(brokenMeteors.size() - 1).setLayoutX(mediumGreyMeteors.get(i).getLayoutX() +
                            (int) (Math.random() * 100) - 50);
                    brokenMeteors.get(brokenMeteors.size() - 1).setLayoutY(mediumGreyMeteors.get(i).getLayoutY() +
                            (int) (Math.random() * 50) - 25);
                    gamePane.getChildren().add(brokenMeteors.get(brokenMeteors.size() - 1));
                }
                gamePane.getChildren().remove(mediumGreyMeteors.get(i));
                mediumGreyMeteors.remove(i);
                removeLife(25);
                createBigMeteor();
            }
        }
    }

    private void removeLife(int damage) {
        playerLife -= damage;
        healthBar.setProgress((double) playerLife / 100);
        healthLabel.setText("HEALTH: " + playerLife + "/100");
        if (playerLife <= 0) {
            client = new Client(nickName, Integer.toString(points));
            String newNickName = client.getNewNickName();
            int newPoints = client.getNewScore();
            database.saveGlobalScores(newNickName, newPoints);
            database.saveLocalScores(nickName, points);
            gameMusic.stop();
            gameStage.close();
            gameTimer.stop();
            menuStage.show();
        }
    }

    private double calculateDistance(double x1, double x2, double y1, double y2) {
        return Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2));
    }

    private void createSounds() {
        gameMusic = new MediaPlayer(new Media(new File(GAME_MUSIC_PATH).toURI().toString()));
        gameMusic.setVolume(0.3);
        gameMusic.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                gameMusic.seek(Duration.ZERO);
            }
        });
        gameMusic.play();

        buttonClickSound = new AudioClip(new File(BUTTON_CLICK_SOUND_PATH).toURI().toString());
        buttonClickSound.setVolume(0.5);

        pauseGameSound = new AudioClip(new File(PAUSE_GAME_SOUND_PATH).toURI().toString());
        pauseGameSound.setVolume(1);

        giveUpStarSound = new AudioClip(new File(GIVE_UP_STAR_SOUND_PATH).toURI().toString());
        giveUpStarSound.setVolume(0.1);

        explosionCrunchSound = new AudioClip(new File(EXPLOSION_CRUNCH_SOUND_PATH).toURI().toString());
        explosionCrunchSound.setVolume(0.1);
    }

    private void toFrontUI() {
        scoresLabel.toFront();
        healthBar.toFront();
        healthLabel.toFront();
    }
}
