package cz.cvut.fel.klykadan.view;

import cz.cvut.fel.klykadan.controller.GameController;
import cz.cvut.fel.klykadan.controller.InputHandler;
import cz.cvut.fel.klykadan.model.CraftingRecipe;
import cz.cvut.fel.klykadan.model.gameObject.characters.Inventory;
import cz.cvut.fel.klykadan.model.gameObject.items.Item;
import cz.cvut.fel.klykadan.model.gameObject.items.SuperWeapon;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * The InventoryUI class handles the display and interaction with the player's inventory.
 */
public class InventoryUI {
    private Pane gamePanel;
    private GUICoinfig cn;
    private Rectangle inventoryPane;
    private GameController controller;
    private InputHandler inputHandler;
    private List<Rectangle> itemSlots;
    private List<ImageView> itemImages;
    private Rectangle cursor;
    private Rectangle currentSlot;
    private int numItemSlots = 15;
    private double paneWidth;
    private double paneHeight;
    private double slotSize;
    private boolean isVisible;
    /**
     * Constructs the InventoryUI with the specified game panel, GUI configuration, game controller, and input handler.
     * @param gamePanel The game panel.
     * @param cn The GUI configuration.
     * @param controller The game controller.
     * @param inputHandler The input handler.
     */

    public InventoryUI(Pane gamePanel, GUICoinfig cn, GameController controller, InputHandler inputHandler){
        this.gamePanel = gamePanel;
        this.cn = cn;
        this.controller = controller;
        this.inputHandler = inputHandler;
        itemSlots = new ArrayList<>();
        itemImages = new ArrayList<>();
        setupInventoryUI();
        setupInventoryCursor();

    }
    private void setupInventoryUI(){
        paneWidth = cn.getScreenWidth() - 500;
        paneHeight = cn.getScreenHeight() - 300;

        inventoryPane = new Rectangle(paneWidth, paneHeight);
        inventoryPane.setFill(Color.rgb(20, 20, 20, 0.7));
        inventoryPane.setStroke(Color.WHITE);
        inventoryPane.setStrokeWidth(3);
        inventoryPane.setX(250);
        inventoryPane.setY(150);
        inventoryPane.setVisible(false);
        gamePanel.getChildren().add(inventoryPane);

        setupItemSlots();
    }
    private void setupItemSlots() {
        double spacing = 10;
        double startX = inventoryPane.getX() + 80;
        double startY = inventoryPane.getY() + 55;
        slotSize = 50;

        itemImages.clear();
        for (int i = 0; i < numItemSlots; i++) {
            double slotX = startX + (i % 5) * (slotSize + spacing);
            double slotY = startY + (i / 5) * (slotSize + spacing);

            Rectangle slot = new Rectangle(slotX, slotY, slotSize, slotSize);
            slot.setStroke(Color.WHITE);
            slot.setFill(Color.TRANSPARENT);
            gamePanel.getChildren().add(slot);
            itemSlots.add(slot);

            ImageView imageView = new ImageView();
            imageView.setX(slotX);
            imageView.setY(slotY);
            imageView.setFitWidth(slotSize);
            imageView.setFitHeight(slotSize);
            imageView.setVisible(false);
            gamePanel.getChildren().add(imageView);
            itemImages.add(imageView);
        }
    }

    private void setupInventoryCursor() {
        cursor = new Rectangle(slotSize, slotSize);
        cursor.setStroke(Color.WHITE);
        cursor.setStrokeWidth(4);
        cursor.setFill(Color.TRANSPARENT);

        if (!itemSlots.isEmpty()) {
            Rectangle firstSlot = itemSlots.get(0);
            cursor.setX(firstSlot.getX());
            cursor.setY(firstSlot.getY());
        }

        gamePanel.getChildren().add(cursor);
    }

    /**
     * Updates the position of the cursor within the inventory based on player input.
     */
    public void updateCursorPosition() {

        int index = itemSlots.indexOf(currentSlot);
        int numColumns = 5;
        if (inputHandler.isUpPressed() && index >= numColumns) {
            index -= numColumns;
        }
        if (inputHandler.isDownPressed() && index < itemSlots.size() - numColumns) {
            index += numColumns;
        }
        if (inputHandler.isLeftPressed() && index % numColumns != 0) {
            index--;
        }
        if (inputHandler.isRightPressed() && index % numColumns != numColumns - 1 && index < itemSlots.size() - 1) {
            index++;
        }

        if (index >= 0 && index < itemSlots.size()) {
            currentSlot = itemSlots.get(index);
            cursor.setX(currentSlot.getX());
            cursor.setY(currentSlot.getY());
        }
    }

    /**
     * Updates the inventory display with the items currently held by the player.
     */
    public void updateInventory() {
        Map<String, List<Item>> inventoryItems = controller.getPlayer().getInventory().getItems();
        int index = 0;
        for (Map.Entry<String, List<Item>> entry : inventoryItems.entrySet()) {  //vsechny ty spojky klic hodnota pomoci entry
            List<Item> items = entry.getValue();
            if (!items.isEmpty()) {
                Item item = items.get(0);
                ImageView imageView = itemImages.get(index);
                imageView.setImage(item.getImage());
                imageView.setVisible(isVisible);
                index++;
            }
        }
        for (int i = index; i < itemImages.size(); i++) {
            itemImages.get(i).setVisible(false);
        }
    }

    /**
     * Attempts to craft a new item from the items currently in the player's inventory.
     */
    private void craft() {
        Inventory inventory = controller.getPlayer().getInventory();
        if (inventory.hasItem("Gun") && inventory.hasItem("Detail")) {
            System.out.println("Items found. Attempting to craft...");
            Item weapon = inventory.getItem("Gun");
            Item detail = inventory.getItem("Detail");
            Item craftedItem = controller.getCraftingRecipe().craft(weapon, detail);

            if (craftedItem != null) {
                System.out.println("Successfully crafted: " + craftedItem.getName());
                inventory.addItem(craftedItem);
                inventory.removeItem(weapon);
                inventory.removeItem(detail);
            } else {
                System.out.println("Crafting failed. Recipe not found.");
            }
        }
    }

    private void showInventory(){
        isVisible = true;
        inventoryPane.setVisible(true);
        cursor.setVisible(true);
        for (Rectangle slot : itemSlots) {
            slot.setVisible(true);
        }
        for (ImageView image : itemImages){
            image.setVisible(true);
        }
        updateInventory();
    }
    private void hideInventory(){
        isVisible = false;
        inventoryPane.setVisible(false);
        cursor.setVisible(false);
        for (Rectangle slot : itemSlots) {
            slot.setVisible(false);
        }
        for (ImageView image : itemImages){
            image.setVisible(false);
        }
    }
    /**
     * Updates the inventory UI based on the current game state.
     */
    public void updateInventoryUI() {
        updateCursorPosition();
        switch (controller.getStateController().getCurrentState()) {
            case START:
                hideInventory();
                break;
            case PLAY:
                hideInventory();
                break;
            case PAUSE:
                hideInventory();
                break;
            case GAME_OVER:
                break;
            case INVENTORY:
                showInventory();
                craft();
                break;
        }
        updateInventory();
    }
}

