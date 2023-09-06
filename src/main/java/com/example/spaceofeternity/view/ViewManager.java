package com.example.spaceofeternity.view;

import com.example.spaceofeternity.data.Database;
import com.example.spaceofeternity.model.*;
import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ViewManager {
    private static final int WIDTH = 1024;
    private static final int HEIGHT = 768;
    private AnchorPane mainPane;
    private Scene mainScene;
    private Stage mainStage;
    private AnimationTimer globalTimer;
    private int time;
    MediaPlayer backgroundMusic;
    AudioClip buttonClickSound;
    AudioClip rejectionSound;
    AudioClip switchSound;

    private String NAME_GAME = "Space of Eternity";
    private String BACKGROUND_IMAGE = "src/main/resources/resources/mainmenu/background.jpg";
    private String LOGO_IMAGE = "src/main/resources/resources/mainmenu/logo.png";
    private String PATH_ICON_IMAGE = "src/main/resources/resources/shipchooser/planerShip.png";
    private String CURSOR_IMAGE = "src/main/resources/resources/mainmenu/cursor.png";
    private String BACKGROUND_MUSIC_PATH = "src/main/resources/resources/sounds/background_music.wav";
    private String BUTTON_CLICK_SOUND_PATH = "src/main/resources/resources/sounds/select.mp3";
    private String REJECTION_SOUND_PATH = "src/main/resources/resources/sounds/rejection.mp3";
    private String SWITCH_SOUND_PATH = "src/main/resources/resources/sounds/switch.mp3";

    private final static int MENU_BUTTONS_START_X = 754;
    private final static int MENU_BUTTONS_START_Y = 200;

    private GameSubScene startSubScene;
    private GameSubScene scoresSubScene;
    private GameSubScene helpSubScene;
    private GameSubScene creditsSubScene;

    private GameSubScene sceneToHide;

    List<GameButton> menuButtons;

    List<ShipPicker> shipsList;
    private ShipTypes choosenShip;

    private NickNameTextField nickName;
    private Database database;
    private ArrayList<String> statisticsTop10;
    private ArrayList<String> statisticRecent;
    private ArrayList<String> statisticTop;

    private ArrayList<ScoresLabel> statisticNickNameTop10 = null;
    private ArrayList<ScoresLabel> statisticScoreTop10 = null;
    private ScoresLabel statisticNickNameRecent = null;
    private ScoresLabel statisticScoreRecent = null;
    private ScoresLabel statisticNickNameTop = null;
    private ScoresLabel statisticScoreTop = null;

    public ViewManager() {
        menuButtons = new ArrayList<>();
        mainPane = new AnchorPane();
        mainScene = new Scene(mainPane, WIDTH, HEIGHT);
        mainStage = new Stage();
        mainStage.setScene(mainScene);
        initializeDatabase();
        setStageProperties();
        createSubScenes();
        createButtons();
        createMenuPanel();
        createBackground();
        createLogo();
        createSounds();
        createLoop();
    }

    private void initializeDatabase() {
        database = new Database();
        statisticsTop10 = new ArrayList<>();
        statisticRecent = new ArrayList<>();
        statisticTop = new ArrayList<>();
    }

    private void setStageProperties() {
        mainStage.getIcons().add(new Image(new File(PATH_ICON_IMAGE).toURI().toString()));
        mainStage.setTitle(NAME_GAME);
        mainStage.getScene().setCursor(new ImageCursor(new Image(new File(CURSOR_IMAGE).toURI().toString())));
    }

    private void showSubScene(GameSubScene subScene) {
        if (sceneToHide != null) {
            sceneToHide.moveSubScene();
        }
        subScene.moveSubScene();
        sceneToHide = subScene;
    }

    private void createSubScenes() {
        startSubScene = new GameSubScene(ButtonTypes.RED);
        scoresSubScene = new GameSubScene(ButtonTypes.GREEN);
        helpSubScene = new GameSubScene(ButtonTypes.BLUE);
        creditsSubScene = new GameSubScene(ButtonTypes.YELLOW);
        mainPane.getChildren().addAll(startSubScene, scoresSubScene, helpSubScene, creditsSubScene);

        createShipChooserSubScene();
        createScoresSubScene();
        createHelpSubScene();
        createCreditsSubScene();
    }

    private void createShipChooserSubScene() {
        InfoLabel chooseShipLabel = new InfoLabel("CHOOSE YOUR SHIP");
        chooseShipLabel.setLayoutX(114);
        chooseShipLabel.setLayoutY(30);
        startSubScene.getPane().getChildren().add(chooseShipLabel);
        startSubScene.getPane().getChildren().add(createShipsToChoose());
        startSubScene.getPane().getChildren().add(createNickNameChoose());
        startSubScene.getPane().getChildren().add(createButtonToStart());
    }

    private FlowPane createShipsToChoose() {
        FlowPane box = new FlowPane();
        box.setHgap(20);
        box.setVgap(20);
        box.setAlignment(Pos.BASELINE_CENTER);
        shipsList = new ArrayList<>();
        for (ShipTypes ship: ShipTypes.values()) {
            ShipPicker shipToPick = new ShipPicker(ship);
            shipsList.add(shipToPick);
            box.getChildren().add(shipToPick);
            shipToPick.setOnMouseClicked(mouseEvent -> {
                for (ShipPicker shipPick: shipsList) {
                    shipPick.setIsCircleChoosen(false);
                }
                switchSound.play();
                shipToPick.setIsCircleChoosen(true);
                choosenShip = shipToPick.getShip();
            });
        }
        box.setLayoutX(98);
        box.setLayoutY(100);
        return box;
    }

    private NickNameTextField createNickNameChoose() {
        nickName = new NickNameTextField("ENTER THE NICKNAME");
        nickName.setLayoutX(40);
        nickName.setLayoutY(410);
        return nickName;
    }

    private GameButton createButtonToStart() {
        GameButton startGameButton = new GameButton("LET'S GO!", ButtonTypes.GREY);
        startGameButton.setLayoutX(370);
        startGameButton.setLayoutY(410);

        startGameButton.setOnAction(actionEvent -> {
            if (choosenShip != null && !nickName.getText().equals("")
                    && !nickName.getText().contains("'")) {
                buttonClickSound.play();
                GameViewManager gameManager = new GameViewManager(nickName.getText());
                gameManager.createNewGame(mainStage, choosenShip);
                mainStageIsShowing(time);
            } else { rejectionSound.play(); }
        });

        return startGameButton;
    }

    private void createScoresSubScene() {
        createScoresArrays();
        createGlobalScores();
        createYourRecentScore();
        createYourTopScore();
    }

    private void createGlobalScores() {
        InfoLabel globalScores = new InfoLabel("GLOBAL SCORES", ButtonTypes.GREEN);
        globalScores.setLayoutX(114);
        globalScores.setLayoutY(30);
        ScoresLabel nickNameLabel1 = new ScoresLabel("NICKNAME");
        ScoresLabel scoreLabel1 = new ScoresLabel("SCORE");
        nickNameLabel1.setLayoutX(-80);
        nickNameLabel1.setLayoutY(74);
        scoreLabel1.setLayoutX(140);
        scoreLabel1.setLayoutY(74);
        scoresSubScene.getPane().getChildren().addAll(globalScores, nickNameLabel1, scoreLabel1);
    }

    private void createScoresArrays() {
        statisticNickNameTop10 = new ArrayList<>();
        statisticScoreTop10 = new ArrayList<>();
        int layoutY = 100;
        for (int j = 0, k = 0; j < 10; j++, k += 2) {
            statisticNickNameTop10.add(new ScoresLabel());
            statisticScoreTop10.add(new ScoresLabel());
            statisticNickNameTop10.get(j).setLayoutX(-80);
            statisticNickNameTop10.get(j).setLayoutY(layoutY);
            statisticScoreTop10.get(j).setLayoutX(140);
            statisticScoreTop10.get(j).setLayoutY(layoutY);
            scoresSubScene.getPane().getChildren().addAll(statisticNickNameTop10.get(j), statisticScoreTop10.get(j));
            layoutY += 20;
        }

        statisticNickNameRecent = new ScoresLabel();
        statisticScoreRecent = new ScoresLabel();
        statisticNickNameRecent.setLayoutX(-80);
        statisticNickNameRecent.setLayoutY(381);
        statisticScoreRecent.setLayoutX(140);
        statisticScoreRecent.setLayoutY(381);
        scoresSubScene.getPane().getChildren().addAll(statisticNickNameRecent, statisticScoreRecent);

        statisticNickNameTop = new ScoresLabel();
        statisticScoreTop = new ScoresLabel();
        statisticNickNameTop.setLayoutX(-80);
        statisticNickNameTop.setLayoutY(482);
        statisticScoreTop.setLayoutX(140);
        statisticScoreTop.setLayoutY(482);
        scoresSubScene.getPane().getChildren().addAll(statisticNickNameTop, statisticScoreTop);
    }

    private void createYourRecentScore() {
        InfoLabel yourRecentScore = new InfoLabel("YOUR RECENT SCORE", ButtonTypes.GREEN, 360, 46, 20);
        yourRecentScore.setLayoutX(125);
        yourRecentScore.setLayoutY(314);
        ScoresLabel nickNameLabel2 = new ScoresLabel("NICKNAME");
        ScoresLabel scoreLabel2 = new ScoresLabel("SCORE");
        nickNameLabel2.setLayoutX(-80);
        nickNameLabel2.setLayoutY(354);
        scoreLabel2.setLayoutX(140);
        scoreLabel2.setLayoutY(354);
        scoresSubScene.getPane().getChildren().addAll(yourRecentScore, nickNameLabel2, scoreLabel2);
    }

    private void createYourTopScore() {
        InfoLabel yourTopScore = new InfoLabel("YOUR TOP SCORE", ButtonTypes.GREEN, 360, 46, 20);
        yourTopScore.setLayoutX(125);
        yourTopScore.setLayoutY(416);
        ScoresLabel nickNameLabel3 = new ScoresLabel("NICKNAME");
        ScoresLabel scoreLabel3 = new ScoresLabel("SCORE");
        nickNameLabel3.setLayoutX(-80);
        nickNameLabel3.setLayoutY(456);
        scoreLabel3.setLayoutX(140);
        scoreLabel3.setLayoutY(456);
        scoresSubScene.getPane().getChildren().addAll(yourTopScore, nickNameLabel3, scoreLabel3);
    }

    private void createHelpSubScene() {
        InfoLabel description = new InfoLabel("DESCRIPTION", ButtonTypes.BLUE);
        description.setLayoutX(114);
        description.setLayoutY(30);
        String textDescription = "    The game was completed as a course work in the discipline 'Programming Technologies and Methods'.\n\n    " +
                "Genre of the game: 'Runner'.\n\n    The essence of the game is that it is necessary to dodge meteorites flying to the " +
                "spaceship, as well as score points by picking up golden stars.";
        HelpLabel helpDescriptionLabel = new HelpLabel(textDescription);
        helpDescriptionLabel.setLayoutX(24);
        helpDescriptionLabel.setLayoutY(76);
        InfoLabel controls = new InfoLabel("CONTROLS", ButtonTypes.BLUE);
        controls.setLayoutX(114);
        controls.setLayoutY(314);
        String textControls = "'A' or 'Left' - move to the left\n" +
        "'D' or 'Right' - move to the right\n'ESC' - pause the game";
        HelpLabel helpControlsLabel = new HelpLabel(textControls);
        helpControlsLabel.setLayoutX(24);
        helpControlsLabel.setLayoutY(360);
        helpSubScene.getPane().getChildren().addAll(description, helpDescriptionLabel, controls, helpControlsLabel);
    }

    private void createCreditsSubScene() {
        String[] textLabels = new String[] {"ART DESIGN", "GAME DESIGN", "PROJECT MANAGER", "SOUND DESIGN",
                "PROGRAMMING","Q&A TESTING", "SPECIAL THANKS"};
        String text = "MILLER VADIM";
        FlowPane globalCredits1 = new FlowPane();
        FlowPane globalCredits2 = new FlowPane();
        FlowPane globalCredits3 = new FlowPane();
        for (int i = 0; i < 3; i++) {
            globalCredits1.getChildren().addAll(new CreditsLabel(textLabels[i]), new SimpleLabel(text));
            globalCredits2.getChildren().addAll(new CreditsLabel(textLabels[i + 3]), new SimpleLabel(text));
        }
        globalCredits3.getChildren().addAll(new CreditsLabel(textLabels[6]), new SimpleLabel(text));
        globalCredits1.setLayoutX(-40);
        globalCredits1.setLayoutY(90);
        globalCredits1.setAlignment(Pos.BASELINE_CENTER);
        globalCredits2.setLayoutX(240);
        globalCredits2.setLayoutY(90);
        globalCredits2.setAlignment(Pos.BASELINE_CENTER);
        globalCredits3.setLayoutX(93);
        globalCredits3.setLayoutY(360);
        globalCredits3.setAlignment(Pos.BASELINE_CENTER);
        creditsSubScene.getPane().getChildren().addAll(globalCredits1, globalCredits2, globalCredits3);
    }

    public Stage getMainStage() { return mainStage; }

    private void addMenuButton(GameButton button) {
        button.setLayoutX(MENU_BUTTONS_START_X);
        button.setLayoutY(MENU_BUTTONS_START_Y + menuButtons.size() * 100);
        menuButtons.add(button);
        mainPane.getChildren().add(button);
    }

    private void createButtons() {
        createStartButton();
        createScoresButton();
        createHelpButton();
        createCreditsButton();
        createExitButton();
    }

    private void createStartButton() {
        GameButton startButton = new GameButton("PLAY", ButtonTypes.RED);
        addMenuButton(startButton);

        startButton.setOnAction(actionEvent -> {
            buttonClickSound.play();
            showSubScene(startSubScene);
        });
    }

    private void createScoresButton() {
        GameButton scoresButton = new GameButton("SCORES", ButtonTypes.GREEN);
        addMenuButton(scoresButton);

        scoresButton.setOnAction(actionEvent -> {
            buttonClickSound.play();
            showSubScene(scoresSubScene);
        });
    }

    private void createHelpButton() {
        GameButton helpButton = new GameButton("HELP", ButtonTypes.BLUE);
        addMenuButton(helpButton);

        helpButton.setOnAction(actionEvent -> {
            buttonClickSound.play();
            showSubScene(helpSubScene);
        });
    }

    private void createCreditsButton() {
        GameButton creditsButton = new GameButton("CREDITS", ButtonTypes.YELLOW);
        addMenuButton(creditsButton);

        creditsButton.setOnAction(actionEvent -> {
            buttonClickSound.play();
            showSubScene(creditsSubScene);
        });
    }

    private void createExitButton() {
        GameButton exitButton = new GameButton("EXIT", ButtonTypes.MAGENTA);
        addMenuButton(exitButton);

        exitButton.setOnAction(actionEvent -> {
            buttonClickSound.play();
            mainStage.close();
        });
    }

    private void createMenuPanel() {
        Rectangle menuPanel = new Rectangle(MENU_BUTTONS_START_X - 40, MENU_BUTTONS_START_Y - 40, 190 + 80, menuButtons.size() * 100 + 40);
        menuPanel.setFill(Color.rgb(125, 125, 125, 0.7));
        menuPanel.setArcHeight(20);
        menuPanel.setArcWidth(20);
        mainPane.getChildren().add(menuPanel);
        menuPanel.toBack();
    }

    private void createBackground() {
        Image backgroundImage = new Image(new File(BACKGROUND_IMAGE).toURI().toString(), WIDTH, HEIGHT, false, true);
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
        mainPane.setBackground(new Background(background));
    }

    private void createLogo() {
        ImageView logo = new ImageView(new Image(new File(LOGO_IMAGE).toURI().toString(), 640, 60, false, true));
        logo.setLayoutX(25);
        logo.setLayoutY(84);

        logo.setOnMouseEntered(mouseEvent -> {
            logo.setEffect(new DropShadow());
        });

        logo.setOnMouseExited(mouseEvent -> {
            logo.setEffect(null);
        });

        mainPane.getChildren().add(logo);
    }

    private void createLoop() {
        updateScoreDatabase();
        time = 0;
        globalTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                time++;
                time = mainStageIsShowing(time);
            }
        };
        globalTimer.start();
    }

    public void updateScoreDatabase() {
        ArrayList<String> newStatisticsTop10 = database.getDataBaseTop10Scores();
        for (int i = 0; i < newStatisticsTop10.size(); i++) {
            if (!statisticsTop10.contains(newStatisticsTop10.get(i))) {
                statisticsTop10 = newStatisticsTop10;
                for (int j = 0, k = 0; k < statisticsTop10.size(); j++, k += 2) {
                    statisticNickNameTop10.get(j).setText(statisticsTop10.get(k));
                    statisticScoreTop10.get(j).setText(statisticsTop10.get(k + 1));
                }
            }
        }

        ArrayList<String> newStatisticsRecent = database.getDataBaseRecentScore();
        for (int i = 0; i < newStatisticsRecent.size(); i++) {
            if (!statisticRecent.contains(newStatisticsRecent.get(i))) {
                statisticRecent = newStatisticsRecent;
                for (int j = 0, k = 0; k < statisticRecent.size(); j++, k += 2) {
                    statisticNickNameRecent.setText(statisticRecent.get(k));
                    statisticScoreRecent.setText(statisticRecent.get(k + 1));
                }
            }
        }

        ArrayList<String> newStatisticsTop = database.getDataBaseTopScore();
        for (int i = 0; i < newStatisticsTop.size(); i++) {
            if (!statisticTop.contains(newStatisticsTop.get(i))) {
                statisticTop = newStatisticsTop;
                for (int j = 0, k = 0; k < statisticTop.size(); j++, k += 2) {
                    statisticNickNameTop.setText(statisticTop.get(k));
                    statisticScoreTop.setText(statisticTop.get(k + 1));
                }
            }
        }
    }

    private int mainStageIsShowing(int time) {
        int thisTime = time;
        if (mainStage.isShowing()) {
            if (thisTime >= 60 * 10) {
                updateScoreDatabase();
                thisTime = 0;
            }
            backgroundMusic.play();
        }
        else { backgroundMusic.stop(); }
        return thisTime;
    }

    private void createSounds() {
        backgroundMusic = new MediaPlayer(new Media(new File(BACKGROUND_MUSIC_PATH).toURI().toString()));
        backgroundMusic.setVolume(0.3);

        buttonClickSound = new AudioClip(new File(BUTTON_CLICK_SOUND_PATH).toURI().toString());
        buttonClickSound.setVolume(0.5);

        rejectionSound = new AudioClip(new File(REJECTION_SOUND_PATH).toURI().toString());
        rejectionSound.setVolume(1);

        switchSound = new AudioClip(new File(SWITCH_SOUND_PATH).toURI().toString());
        switchSound.setVolume(1);
    }
}
