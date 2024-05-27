package cz.cvut.fel.klykadan.controller;

import java.io.Serializable;
import java.util.*;
/**
 * The SaveController class encapsulates the data required to save and load game states.
 * It includes player information, inventory items, and game object states, allowing the game
 * to be restored to a specific state. This class and its nested classes are serializable to support
 * easy persistence to and from files.
 */

public class SaveController implements Serializable {
    private int playerX, playerY, playerHealth;
    private int playerLevel;
    private List<ItemData> inventory = new ArrayList<>();
    private List<GameObjectData> gameObjectData = new ArrayList<>();
    private Set<UUID> removedItems = new HashSet<>();

    public int getPlayerX() { return playerX; }
    public void setPlayerX(int playerX) { this.playerX = playerX; }

    public int getPlayerY() { return playerY; }
    public void setPlayerY(int playerY) { this.playerY = playerY; }

    public int getPlayerHealth() { return playerHealth; }
    public void setPlayerHealth(int playerHealth) { this.playerHealth = playerHealth; }

    public int getPlayerLevel() { return playerLevel; }
    public void setPlayerLevel(int playerLevel) { this.playerLevel = playerLevel; }

    public List<ItemData> getInventory() {  return inventory; }
    public List<GameObjectData> getGameObjectData() {
        return gameObjectData;
    }

    public Set<UUID> getRemovedItems() {
        return removedItems;
    }

    public void setRemovedItems(Set<UUID> removedItems) {
        this.removedItems = removedItems;
    }

    /**
     * Serializable class to hold data about items, including their position, size, usage status, and visibility.
     */
    public static class ItemData implements Serializable {
        String name;
        int x, y, sizeX, sizeY;
        boolean used;
        boolean isVisible;
        int ammoCount;
        UUID id;

        public ItemData(UUID id,String name, int x, int y, int sizeX, int sizeY, boolean used, int ammoCount, boolean isVisible) {
            this.id = id;
            this.name = name;
            this.x = x;
            this.y = y;
            this.sizeX = sizeX;
            this.sizeY = sizeY;
            this.used = used;
            this.ammoCount = ammoCount;
            this.isVisible = isVisible;
        }
    }
    /**
     * Serializable class to store data about generic game objects, including their position, size, and visibility.
     */

    public static class EnemyData implements Serializable {
        int x, y, health;
        boolean isMoving;

        public EnemyData(int x, int y, int health, boolean isMoving, boolean visible) {
            this.x = x;
            this.y = y;
            this.health = health;
            this.isMoving = isMoving;
        }
    }
    /**
     * Serializable class to store data about generic game objects, including their position, size, and visibility.
     */
    public static class GameObjectData implements Serializable {
        String name;
        int x, y, sizeX, sizeY;
        boolean isVisible;

        public GameObjectData(String name, int x, int y, int sizeX, int sizeY, boolean isVisible) {
            this.name = name;
            this.x = x;
            this.y = y;
            this.sizeX = sizeX;
            this.sizeY = sizeY;
            this.isVisible = isVisible;
        }
    }
}