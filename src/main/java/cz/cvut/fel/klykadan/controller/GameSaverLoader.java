package cz.cvut.fel.klykadan.controller;

import cz.cvut.fel.klykadan.model.gameObject.Door;
import cz.cvut.fel.klykadan.model.gameObject.GameObject;
import cz.cvut.fel.klykadan.model.gameObject.LevelDoor;
import cz.cvut.fel.klykadan.model.gameObject.characters.Enemy;
import cz.cvut.fel.klykadan.model.gameObject.characters.Player;
import cz.cvut.fel.klykadan.model.gameObject.items.*;
import cz.cvut.fel.klykadan.view.GUICoinfig;
import javafx.scene.image.Image;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

/**
 * The GameSaverLoader class provides singleton access to game saving and loading functionalities.
 * It handles serialization of game state to a file and deserialization back from the file.
 * This class ensures that only one instance of itself is created to maintain a consistent access point
 * for saving and loading across the application.
 */
public class GameSaverLoader {
    private static GameSaverLoader instance;
    private static final Logger LOGGER = Logger.getLogger(GameSaverLoader.class.getName());

    private GameSaverLoader() {
    }
    /**
     * Returns the singleton instance of the GameSaverLoader.
     * If the instance does not exist, it is created in a thread-safe manner.
     *
     * @return the singleton instance of GameSaverLoader
     */
    public static GameSaverLoader getInstance() {
        if (instance == null) {
            synchronized (GameSaverLoader.class) {
                if (instance == null) {
                    instance = new GameSaverLoader();
                }
            }
        }
        return instance;
    }
    /**
     * Saves the current game state to a file using Java serialization.
     * It captures the entire game state in a SaveController object.
     *
     * @param data the SaveController object containing game state to be saved
     */
    public void saveGameStreamer(SaveController data) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("savegame.dat"))) {
            out.writeObject(data);
            LOGGER.info("Game saved");
        } catch (IOException e) {
            LOGGER.severe("Cannot save: " + e.getMessage());
        }
    }
    /**
     * Loads the game state from a file using Java serialization.
     * It returns a SaveController object which contains the loaded game state.
     *
     * @return the SaveController object containing the loaded game state, or null if loading fails
     */

    public SaveController loadGameReader() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("savegame.dat"))) {
            LOGGER.info("Game loaded");
            return (SaveController) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.severe("Cannot load: " + e.getMessage());
            return null;
        }
    }
    /**
     * Saves the game by capturing the current state from the GameController and other components.
     * This includes player data and the state of all game objects and NPCs.
     *
     * @param controller the main game controller from which to save the state
     * @param cn GUI configuration used in the game
     */
    public void saveGame(GameController controller, GUICoinfig cn) {
        SaveController saveData = new SaveController();
        Player player = controller.getPlayer();
        saveData.setPlayerX(player.getX());
        saveData.setPlayerY(player.getY());
        saveData.setPlayerHealth(player.getHealth());
        saveData.setPlayerLevel(player.getCurrentLevel());
        LOGGER.info("Saved with: " + player.getCurrentLevel());


        Set<UUID> removedItems = player.getInventory().getRemovedItems();
        saveData.setRemovedItems(new HashSet<>(removedItems));

        player.getInventory().getAllItems().stream()
                .filter(item -> !removedItems.contains(item.getId()))
                .forEach(item -> {
                    int ammoCount = 0;
                    if (item instanceof Weapon) {
                        ammoCount = ((Weapon) item).getAmmoCount();
                    } else if (item instanceof SuperWeapon) {
                        ammoCount = ((SuperWeapon) item).getAmmoCount();
                    }
                    saveData.getInventory().add(new SaveController.ItemData(
                            item.getId(),
                            item.getName(),
                            item.getXposition(), item.getYposition(),
                            item.getSizeX(), item.getSizeY(),
                            false,
                            ammoCount,
                            item.isVisible()
                    ));
                });
        controller.getGameObjects().forEach(obj -> {
            if (obj instanceof Door) {
                saveData.getGameObjectData().add(new SaveController.GameObjectData(
                        obj.getName(), obj.getXposition(), obj.getYposition(),
                        obj.getSizeX(), obj.getSizeY(), obj.isVisible()
                ));
            }
        });
        LOGGER.info("Saving removed items: " + removedItems);
        saveGameStreamer(saveData);
    }
    /**
     * Loads the game by restoring the state to the GameController and other components
     * from a previously saved state.
     *
     * @param controller the main game controller into which the state is loaded
     * @param cn GUI configuration used in the game
     */
    public void loadGame(GameController controller, GUICoinfig cn) {
        SaveController saveData = loadGameReader();
        if (saveData != null) {
            Player player = controller.getPlayer();
            player.setX(saveData.getPlayerX());
            player.setY(saveData.getPlayerY());
            player.setHealth(saveData.getPlayerHealth());
            player.setCurrentLevel(saveData.getPlayerLevel());
            LOGGER.info("Loaded with level: " + saveData.getPlayerLevel());
            controller.switchToNextLevel(saveData.getPlayerLevel() - 1);

            player.getInventory().clear();
            Set<UUID> removedItems = saveData.getRemovedItems();
            Map<String, Boolean> itemVisibilityMap = new HashMap<>();
            saveData.getInventory().forEach(itemData -> {
                itemVisibilityMap.put(itemData.name, itemData.isVisible);
                Item item = createItemFromData(itemData, cn);
                if (item != null) {
                    player.getInventory().addItem(item);
                }
            });
            controller.getGameObjects().forEach(obj -> {
                if (itemVisibilityMap.containsKey(obj.getName())) {
                    obj.setVisible(itemVisibilityMap.get(obj.getName()));
                }
                if (removedItems.contains(obj.getId())) {
                    obj.setVisible(false);
                }
                if(obj instanceof Door){
                    obj.setVisible(false);
                }
            });
            saveData.getGameObjectData().forEach(gameObjectData -> {
                GameObject obj = createGameObjectFromData(gameObjectData, cn);
                if (obj != null) {
                    controller.getGameObjects().add(obj);
                }
            });
            LOGGER.info("Loaded removed items: " + removedItems);
            controller.getCraftingRecipe().updateRecipe(controller, cn);
        }
    }

    private Item createItemFromData(SaveController.ItemData itemData, GUICoinfig cn) {
        Image image = loadImage(itemData.name);
        Item item;
        switch (itemData.name) {
            case "Gun":
                item = new Weapon(cn, itemData.x, itemData.y, itemData.sizeX, itemData.sizeY);
                ((Weapon) item).setAmmoCount(itemData.ammoCount);
                break;
            case "SuperWeapon":
                item = new SuperWeapon(cn);
                ((SuperWeapon) item).setAmmoCount(itemData.ammoCount);
                break;
            case "Heal":
                item = new Heal(image, itemData.x, itemData.y, itemData.sizeX, itemData.sizeY);
                break;
            case "Ammo":
                item = new Ammo(image, itemData.x, itemData.y, itemData.sizeX, itemData.sizeY);
                break;
            default:
                item = new Item(itemData.name, image, itemData.x, itemData.y, itemData.sizeX, itemData.sizeY);
        }
        item.setVisible(itemData.isVisible);
        item.setId(itemData.id);
        return item;
    }
    private GameObject createGameObjectFromData(SaveController.GameObjectData data, GUICoinfig cn) {
        Image image = loadImage(data.name);
        if ("Door".equals(data.name)) {
            return new Door(image, data.x, data.y, data.sizeX, data.sizeY, true);
        } else if ("LevelDoor".equals(data.name)){
            return new LevelDoor(data.x, data.y, data.sizeX, data.sizeY, true);
        }
        return null;
    }
    /**
     * Loads an image based on the item name, providing a centralized method to fetch item images.
     *
     * @param itemName the name of the item for which to load the image
     * @return the loaded Image object
     * @throws IllegalArgumentException if no image path is found for the given item name
     */
    public Image loadImage(String itemName) {
        String path;
        switch (itemName) {
            case "Key":
                path = "/textures/keys/doorKey.png";
                break;
            case "Door":
                path = "/textures/doors/door_red.png";
                break;
            case "Gun":
                path = "/textures/weapon/gun2.png";
                break;
            case "SuperWeapon":
                path = "/textures/weapon/gun3.png";
                break;
            case "Heal":
                path = "/textures/heal/heal.png";
                break;
            case "LevelKey":
                path = "/textures/keys/levelKey.png";
                break;
            case "Detail":
                path = "/textures/detail/detail.png";
                break;
            case "Ammo":
                path = "/textures/weapon/ammo.png";
                break;
            case "LevelDoor":
                path = "/textures/lab/tile050.png";
                break;
            default:
                throw new IllegalArgumentException("No image path found: " + itemName);
        }
        return new Image(path);
    }
}
