package cz.cvut.fel.klykadan.view;

import cz.cvut.fel.klykadan.controller.GameController;
import cz.cvut.fel.klykadan.controller.GameStateController;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
/**
 * The GameOverUI class handles the game over screen, providing options to start a new game, load a game, or return to the start menu.
 */
public class GameOverUI {
    private Pane gamePanel;
    private GUICoinfig cn;
    private Label gameOverLabel;
    private Button startNewGameButton, startMenuButton, loadGameButton;
    private Rectangle backgroundGameOverMenu;
    private GameController controller;


    public GameOverUI(Pane gamePanel, GUICoinfig cn, GameController controller){
        this.gamePanel = gamePanel;
        this.cn = cn;
        this.controller = controller;
        setupGameOverMenu();
    }
    private void setupGameOverMenu(){
        backgroundGameOverMenu = new Rectangle(0, 0, cn.getScreenWidth(), cn.getScreenHeight());
        backgroundGameOverMenu.setFill(Color.rgb(20, 20, 20, 0.5));
        backgroundGameOverMenu.setEffect(new GaussianBlur(10));
        gamePanel.getChildren().add(backgroundGameOverMenu);
        backgroundGameOverMenu.setVisible(false);

        Font pixelFont = Font.loadFont(getClass().getResourceAsStream("/fonts/pixel.ttf"), 58);
        gameOverLabel = new Label("GameOver");
        gameOverLabel.setFont(pixelFont);
        gameOverLabel.setTextFill(Color.RED);
        gameOverLabel.setLayoutY(120);
        gamePanel.getChildren().add(gameOverLabel);
        gameOverLabel.setVisible(false);

        gameOverLabel.layoutXProperty().bind(gamePanel.widthProperty().subtract(gameOverLabel.widthProperty()).divide(2));

        startNewGameButton = new Button("New Game");
        styleButton(startNewGameButton);
        startNewGameButton.setLayoutX(cn.getScreenWidth() / 2 - 100);
        startNewGameButton.setLayoutY(250);

        loadGameButton = new Button("Load Game");
        styleButton(loadGameButton);
        loadGameButton.setLayoutX(cn.getScreenWidth() / 2 - 100);
        loadGameButton.setLayoutY(300);

        startMenuButton = new Button("Start Menu");
        styleButton(startMenuButton);
        startMenuButton.setLayoutX(cn.getScreenWidth() / 2 - 100);
        startMenuButton.setLayoutY(350);

        gamePanel.getChildren().addAll(startNewGameButton, loadGameButton, startMenuButton);
        startNewGameButton.setVisible(false);
        loadGameButton.setVisible(false);
        startMenuButton.setVisible(false);


        startNewGameButton.setOnAction(e -> {
            controller.resetGame();
            controller.getStateController().setCurrentState(GameStateController.State.PLAY);
        });
        loadGameButton.setOnAction(e -> {
            controller.loadGame();
            controller.getStateController().setCurrentState(GameStateController.State.PLAY);
        });
        startMenuButton.setOnAction(e -> {
            controller.getStateController().setCurrentState(GameStateController.State.START);
        });
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
    private void showGameOverMenu() {
        backgroundGameOverMenu.setVisible(true);
        gameOverLabel.setVisible(true);
        startNewGameButton.setVisible(true);
        startNewGameButton.setDisable(false);
        loadGameButton.setVisible(true);
        loadGameButton.setDisable(false);
        startMenuButton.setVisible(true);
        startMenuButton.setDisable(false);
        gamePanel.requestFocus();
    }
    private void hideGameOverMenu() {
        backgroundGameOverMenu.setVisible(false);
        backgroundGameOverMenu.setDisable(true);
        gameOverLabel.setVisible(false);
        gameOverLabel.setDisable(true);
        startNewGameButton.setVisible(false);
        startNewGameButton.setDisable(true);
        loadGameButton.setVisible(false);
        loadGameButton.setDisable(true);
        startMenuButton.setVisible(false);
        startMenuButton.setDisable(true);
        gamePanel.requestFocus();
    }
    /**
     * Updates the game over UI based on the current game state.
     */
    public void updateGameOverUI() {
        switch (controller.getStateController().getCurrentState()) {
            case START:
                hideGameOverMenu();
                break;
            case PLAY:
                hideGameOverMenu();
                break;
            case PAUSE:
                hideGameOverMenu();
                break;
            case GAME_OVER:
                showGameOverMenu();
                break;
            case INVENTORY:
                hideGameOverMenu();
                break;
        }
    }
}
