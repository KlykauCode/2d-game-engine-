package cz.cvut.fel.klykadan;

import cz.cvut.fel.klykadan.controller.GameController;
import cz.cvut.fel.klykadan.controller.InputHandler;
import cz.cvut.fel.klykadan.model.gameObject.characters.Player;
import cz.cvut.fel.klykadan.view.GUICoinfig;
import cz.cvut.fel.klykadan.view.GamePanel;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) {
        GUICoinfig cn = new GUICoinfig();
        stage.setTitle("Zero Point");

        InputHandler input = new InputHandler();
        GamePanel gamePanel = new GamePanel(cn, input);


        VBox root = new VBox(gamePanel);
        root.setFocusTraversable(true);
        root.setOnKeyPressed(input);
        root.setOnKeyReleased(input);

        Scene scene = new Scene(root, cn.getScreenWidth(), cn.getScreenHeight());

        stage.setScene(scene);

        stage.setResizable(false);
        stage.show();
        gamePanel.requestFocus();
    }

    public static void main(String[] args) {
        launch();
    }
}

