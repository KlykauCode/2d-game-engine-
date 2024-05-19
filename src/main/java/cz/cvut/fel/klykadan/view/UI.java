package cz.cvut.fel.klykadan.view;

import cz.cvut.fel.klykadan.controller.GameController;
import cz.cvut.fel.klykadan.controller.InputHandler;
import cz.cvut.fel.klykadan.model.gameObject.characters.Player;
import cz.cvut.fel.klykadan.model.gameObject.items.SuperWeapon;
import cz.cvut.fel.klykadan.model.gameObject.items.Weapon;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
/**
 * The UI class handles the user interface elements of the game.
 * It includes methods to set up and update the dialogue, health, and weapon UI components.
 */
public class UI {
    private Pane gamePanel;
    private GUICoinfig cn;
    private Label dialogueLabel;
    private Label healthLabel;
    private ImageView weaponIcon;
    private ImageView superWeaponIcon;
    private Label ammoLabel;
    private Rectangle dialogueBackground;
    private GameController controller;
    private InputHandler inputHandler;
    private InventoryUI inventoryUI;
    private GameOverUI gameOverUI;
    private MenuUI menuUI;
    /**
     * Constructs the UI class with the specified parameters.
     * @param gamePanel The game panel.
     * @param cn The GUI configuration.
     * @param controller The game controller.
     * @param inputHandler The input handler.
     */
    public UI(Pane gamePanel, GUICoinfig cn, GameController controller, InputHandler inputHandler) {
        this.gamePanel = gamePanel;
        this.cn = cn;
        this.controller = controller;
        this.inputHandler = inputHandler;
        inventoryUI = new InventoryUI(gamePanel, cn, controller, inputHandler);
        menuUI = new MenuUI(gamePanel, cn, controller);
        gameOverUI = new GameOverUI(gamePanel, cn, controller);
        drawScreens();
        updateUI();
    }
    private void drawScreens() {
        controller.getStateController().update();
        setupDialogueUI();
        setupHealthUI();
        setupWeaponUI();
    }
    private void setupWeaponUI() {
        weaponIcon = new ImageView(new Image("/textures/weapon/gun2.png"));
        weaponIcon.setFitWidth(64);
        weaponIcon.setFitHeight(64);
        weaponIcon.setLayoutX(10);
        weaponIcon.setLayoutY(10);
        weaponIcon.setVisible(false);

        superWeaponIcon = new ImageView(new Image("/textures/weapon/gun3.png"));
        superWeaponIcon.setFitWidth(64);
        superWeaponIcon.setFitHeight(64);
        superWeaponIcon.setLayoutX(10);
        superWeaponIcon.setLayoutY(10);
        superWeaponIcon.setVisible(false);

        Font pixelFont = Font.loadFont(getClass().getResourceAsStream("/fonts/pixel.ttf"), 15);

        ammoLabel = new Label();
        ammoLabel.setTextFill(Color.WHITE);
        ammoLabel.setFont(pixelFont);
        ammoLabel.setLayoutX(85);
        ammoLabel.setLayoutY(25);
        gamePanel.getChildren().addAll(weaponIcon, superWeaponIcon, ammoLabel);
    }

    private void setupHealthUI() {
        Font pixelFontsmall = Font.loadFont(getClass().getResourceAsStream("/fonts/pixel.ttf"), 30);
        healthLabel = new Label();
        healthLabel.setTextFill(Color.WHITE);
        healthLabel.setFont(pixelFontsmall);
        healthLabel.setLayoutX(cn.getScreenWidth() - 150);
        healthLabel.setLayoutY(cn.getScreenHeight() - 70);
        gamePanel.getChildren().add(healthLabel);
    }

    private void setupDialogueUI() {
        dialogueBackground = new Rectangle(50, cn.getScreenHeight() - 120, cn.getScreenWidth() - 100, 100);
        dialogueBackground.setFill(Color.BLACK);
        dialogueBackground.setOpacity(0.7);
        dialogueBackground.setVisible(false);
        gamePanel.getChildren().add(dialogueBackground);

        dialogueLabel = new Label();
        dialogueLabel.setTextFill(Color.WHITE);
        dialogueLabel.setFont(new Font(18));
        dialogueLabel.setLayoutX(65);
        dialogueLabel.setLayoutY(cn.getScreenHeight() - 110);
        dialogueLabel.setWrapText(true);
        dialogueLabel.setMaxWidth(cn.getScreenWidth() - 130);
        dialogueLabel.setVisible(false);
        gamePanel.getChildren().add(dialogueLabel);
    }
    /**
     * Shows a dialogue with the specified text.
     * @param text The dialogue text to display.
     */
    public void showDialogue(String text) {
        dialogueLabel.setText(text);
        dialogueLabel.setVisible(true);
        dialogueBackground.setVisible(true);
    }

    /**
     * Updates the health display with the specified health value.
     * @param health The current health of the player.
     */
    public void updateHealth(int health) {
        healthLabel.setText("" + health + " HP");
    }


    /**
     * Updates the weapon UI based on the player's current weapon.
     */
    public void updateWeaponUI() {
        Player player = controller.getPlayer();
        Weapon weapon = player.getWeapon();
        SuperWeapon superWeapon = player.getSuperWeapon();

        if (player.getInventory().hasItem("SuperWeapon") && superWeapon != null ) {
            superWeaponIcon.setVisible(true);
            weaponIcon.setVisible(false);
            ammoLabel.setText(superWeapon.getAmmoCount() + " SP");
            ammoLabel.setVisible(true);
        } else if (player.getInventory().hasItem("Gun") && weapon != null) {
            weaponIcon.setVisible(true);
            superWeaponIcon.setVisible(false);
            ammoLabel.setText(weapon.getAmmoCount() + " REG");
            ammoLabel.setVisible(true);
        } else {
            weaponIcon.setVisible(false);
            superWeaponIcon.setVisible(false);
            ammoLabel.setVisible(false);
        }
    }
    /**
     * Hides the dialogue UI.
     */
    public void hideDialogue() {
        dialogueLabel.setVisible(false);
        dialogueBackground.setVisible(false);
    }

    /**
     * Updates the UI based on the current game state.
     */
    public void updateUI() {
        switch (controller.getStateController().getCurrentState()) {
            case START:
                healthLabel.setVisible(false);
                weaponIcon.setVisible(false);
                superWeaponIcon.setVisible(false);
                ammoLabel.setVisible(false);
                hideDialogue();
                break;
            case PLAY:
                healthLabel.setVisible(true);
                updateWeaponUI();
                break;
            case PAUSE:
                hideDialogue();
                healthLabel.setVisible(false);
                break;
            case GAME_OVER:
                weaponIcon.setVisible(false);
                superWeaponIcon.setVisible(false);
                ammoLabel.setVisible(false);
                healthLabel.setVisible(false);
                hideDialogue();
                break;
            case INVENTORY:
                hideDialogue();
                break;
        }
        updateHealth(controller.getPlayer().getHealth());
        menuUI.updateMenuUI();
        inventoryUI.updateInventoryUI();
        gameOverUI.updateGameOverUI();
    }
}
