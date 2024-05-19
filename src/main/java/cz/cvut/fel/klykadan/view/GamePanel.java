package cz.cvut.fel.klykadan.view;

import cz.cvut.fel.klykadan.controller.GameController;
import cz.cvut.fel.klykadan.controller.InputHandler;
import cz.cvut.fel.klykadan.controller.TextureManager;
import cz.cvut.fel.klykadan.model.gameObject.characters.Player;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.io.IOException;
/**
 * The GamePanel class represents the main game panel, where all game elements are drawn.
 * It extends the Pane class and initializes the game controller and user interface.
 */
public class GamePanel extends Pane {
    private GUICoinfig cn;
    private Canvas canvas;
    private GraphicsContext gc;
    private InputHandler input;
    private GameController gameController;
    private UI UI;
    /**
     * Constructs the GamePanel with the specified configuration and input handler.
     * @param cn The GUI configuration.
     * @param input The input handler for user inputs.
     */
    public GamePanel(GUICoinfig cn, InputHandler input){
        this.cn = cn;
        this.input = input;
        this.setPrefSize(cn.getScreenWidth(),cn.getScreenHeight());
        this.setStyle("-fx-background-color: black;");

        canvas = new Canvas(cn.getScreenWidth(), cn.getScreenHeight());
        gc = canvas.getGraphicsContext2D();
        this.getChildren().add(canvas);


        gameController = new GameController(cn, input, gc, null);
        UI = new UI(this, cn, gameController, input);
        gameController.setUI(UI);

        this.setOnKeyPressed(input);
        gameController.startGameLoop();
    }
}

