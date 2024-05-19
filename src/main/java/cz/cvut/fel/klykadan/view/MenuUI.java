package cz.cvut.fel.klykadan.view;

import cz.cvut.fel.klykadan.controller.GameController;
import cz.cvut.fel.klykadan.controller.GameStateController;
import cz.cvut.fel.klykadan.controller.InputHandler;
import cz.cvut.fel.klykadan.model.gameObject.characters.Player;
import cz.cvut.fel.klykadan.model.gameObject.items.Weapon;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
/**
 * The MenuUI class handles the display and interaction with the game's start and pause menus.
 */
public class MenuUI {
    private Pane gamePanel;
    private GUICoinfig cn;
    private Label titleLabel;
    private Label menuLabel;
    private Button startButton, saveButton, playMenuButton, loadMenuButton;
    private Button newGameButton, loadButton, exitButton;
    private ImageView background;
    private Rectangle backgroundRectMenu;
    private GameController controller;
    /**
     * Constructs the MenuUI with the specified game panel, GUI configuration, and game controller.
     * @param gamePanel The game panel.
     * @param cn The GUI configuration.
     * @param controller The game controller.
     */


    public MenuUI(Pane gamePanel, GUICoinfig cn, GameController controller){
        this.gamePanel = gamePanel;
        this.cn = cn;
        this.controller = controller;

        setupStartScreen();
        setupPauseMenuScreen();
    }

    private void setupPauseMenuScreen(){
        backgroundRectMenu = new Rectangle(0, 0, cn.getScreenWidth(), cn.getScreenHeight());
        backgroundRectMenu.setFill(Color.rgb(20, 20, 20, 0.3));
        backgroundRectMenu.setEffect(new GaussianBlur(10));
        gamePanel.getChildren().add(backgroundRectMenu);
        backgroundRectMenu.setVisible(false);

        Font pixelFont = Font.loadFont(getClass().getResourceAsStream("/fonts/pixel.ttf"), 48);
        menuLabel = new Label("PAUSE");
        menuLabel.setFont(pixelFont);
        menuLabel.setTextFill(Color.WHITE);
        menuLabel.setLayoutY(120);
        gamePanel.getChildren().add(menuLabel);
        menuLabel.setVisible(false);

        menuLabel.layoutXProperty().bind(gamePanel.widthProperty().subtract(menuLabel.widthProperty()).divide(2));

        playMenuButton = new Button("Play");
        styleButton(playMenuButton);
        playMenuButton.setLayoutX(cn.getScreenWidth() / 2 - 100);
        playMenuButton.setLayoutY(250);

        loadMenuButton = new Button("Load Game");
        styleButton(loadMenuButton);
        loadMenuButton.setLayoutX(cn.getScreenWidth() / 2 - 100);
        loadMenuButton.setLayoutY(300);

        saveButton = new Button("Save Game");
        styleButton(saveButton);
        saveButton.setLayoutX(cn.getScreenWidth() / 2 - 100);
        saveButton.setLayoutY(350);

        startButton = new Button("Start Menu");
        styleButton(startButton);
        startButton.setLayoutX(cn.getScreenWidth() / 2 - 100);
        startButton.setLayoutY(400);

        gamePanel.getChildren().addAll(playMenuButton, loadMenuButton, saveButton, startButton);
        playMenuButton.setVisible(false);
        loadMenuButton.setVisible(false);
        saveButton.setVisible(false);
        startButton.setVisible(false);


        playMenuButton.setOnAction(e -> {
            controller.getStateController().setCurrentState(GameStateController.State.PLAY);
        });
        loadMenuButton.setOnAction(e -> {
            controller.loadGame();
            controller.getStateController().setCurrentState(GameStateController.State.PLAY);
        });
        saveButton.setOnAction(e -> {
            controller.saveGame();});

        startButton.setOnAction(e -> {
            controller.getStateController().setCurrentState(GameStateController.State.START);
        });
    }
    private void setupStartScreen() {
        background = new ImageView(new Image("backgrounds/menu1p.png"));
        background.setFitWidth(cn.getScreenWidth() + 10);
        background.setFitHeight(cn.getScreenHeight() + 10);
        background.setEffect(new GaussianBlur(10));
        background.setVisible(false);
        gamePanel.getChildren().add(background);

        Font pixelFont = Font.loadFont(getClass().getResourceAsStream("/fonts/pixel.ttf"), 58);

        titleLabel = new Label("ZERO POINT");
        titleLabel.setFont(pixelFont);
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setLayoutY(120);
        gamePanel.getChildren().add(titleLabel);

        titleLabel.layoutXProperty().bind(gamePanel.widthProperty().subtract(titleLabel.widthProperty()).divide(2));

        newGameButton = new Button("New Game");
        styleButton(newGameButton);
        newGameButton.setLayoutX(cn.getScreenWidth() / 2 - 100);
        newGameButton.setLayoutY(250);

        loadButton = new Button("Load Game");
        styleButton(loadButton);
        loadButton.setLayoutX(cn.getScreenWidth() / 2 - 100);
        loadButton.setLayoutY(300);

        exitButton = new Button("Exit");
        styleButton(exitButton);
        exitButton.setLayoutX(cn.getScreenWidth() / 2 - 100);
        exitButton.setLayoutY(350);

        gamePanel.getChildren().addAll(newGameButton, loadButton, exitButton);


        newGameButton.setOnAction(e -> {
            controller.resetGame();
            controller.getStateController().setCurrentState(GameStateController.State.PLAY);
        });
        loadButton.setOnAction(e -> {
            controller.loadGame();
            controller.getStateController().setCurrentState(GameStateController.State.PLAY);
        });
        exitButton.setOnAction(e -> {
            Platform.exit();
            controller.clearEnemies();
            System.exit(0);});
    }
    private void styleButton(Button button) {
        Font pixelFontsmall = Font.loadFont(getClass().getResourceAsStream("/fonts/pixel.ttf"), 16);
        button.setStyle("-fx-background-color: rgba(102, 102, 102, 0.5); -fx-border-color: white; -fx-border-width: 2;");
        button.setTextFill(Color.WHITE);
        button.setFont(pixelFontsmall);
        button.setPrefWidth(200);
        button.setPrefHeight(40);

        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-border-color: white; -fx-border-width: 2;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: rgba(102, 102, 102, 0.5); -fx-text-fill: white; -fx-border-color: white; -fx-border-width: 2;"));
    }


    private void showMenu() {
        backgroundRectMenu.setVisible(true);
        menuLabel.setVisible(true);
        playMenuButton.setVisible(true);
        playMenuButton.setDisable(false);
        loadMenuButton.setVisible(true);
        loadMenuButton.setDisable(false);
        saveButton.setVisible(true);
        saveButton.setDisable(false);
        startButton.setVisible(true);
        startButton.setDisable(false);
        gamePanel.requestFocus();
    }
    private void hideMenu() {
        backgroundRectMenu.setVisible(false);
        backgroundRectMenu.setDisable(true);
        menuLabel.setVisible(false);
        menuLabel.setDisable(true);
        playMenuButton.setVisible(false);
        playMenuButton.setDisable(true);
        loadMenuButton.setVisible(false);
        loadMenuButton.setDisable(true);
        startButton.setVisible(false);
        startButton.setDisable(true);
        saveButton.setVisible(false);
        saveButton.setDisable(true);
        gamePanel.requestFocus();
    }
    private void showStartMenu(){
        background.setVisible(true);
        background.setDisable(false);
        titleLabel.setDisable(false);
        titleLabel.setVisible(true);
        newGameButton.setVisible(true);
        newGameButton.setDisable(false);
        loadButton.setVisible(true);
        loadButton.setDisable(false);
        exitButton.setVisible(true);
        exitButton.setDisable(false);
    }
    private void hideStartMenu(){
        background.setVisible(false);
        background.setDisable(true);
        titleLabel.setVisible(false);
        titleLabel.setDisable(true);
        newGameButton.setVisible(false);
        newGameButton.setDisable(true);
        loadButton.setVisible(false);
        loadButton.setDisable(true);
        exitButton.setVisible(false);
        exitButton.setDisable(true);
        gamePanel.requestFocus();
    }

    /**
     * Updates the menu UI based on the current game state.
     */
    public void updateMenuUI() {
        switch (controller.getStateController().getCurrentState()) {
            case START:
                showStartMenu();
                hideMenu();
                break;
            case PLAY:
                hideStartMenu();
                hideMenu();
                break;
            case PAUSE:
                hideStartMenu();
                showMenu();
                break;
            case GAME_OVER:
                break;
            case INVENTORY:
                break;
        }
    }

}
