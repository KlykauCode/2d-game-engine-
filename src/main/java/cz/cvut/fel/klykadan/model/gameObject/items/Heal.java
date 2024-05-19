package cz.cvut.fel.klykadan.model.gameObject.items;

import cz.cvut.fel.klykadan.model.gameObject.characters.Player;
import javafx.scene.image.Image;
/**
 * Represents a healing item that restores the player's health when used.
 * This class extends the Item class.
 */
public class Heal extends Item{
    private int hpHeal;
    /**
     * Constructs a Heal object with the specified initial position and size.
     * @param initialX The initial X position of the Heal item.
     * @param initialY The initial Y position of the Heal item.
     * @param sizeX The size of the Heal item along the X axis.
     * @param sizeY The size of the Heal item along the Y axis.
     */

    public Heal(int initialX, int initialY, int sizeX, int sizeY) {
        this(new Image("/textures/heal/heal.png"), initialX, initialY, sizeX, sizeY);
    }
    /**
     * Constructs a Heal object with the specified image, position, and size.
     * @param image The image of the Heal item.
     * @param Xposition The X position of the Heal item.
     * @param Yposition The Y position of the Heal item.
     * @param sizeX The size of the Heal item along the X axis.
     * @param sizeY The size of the Heal item along the Y axis.
     */
    public Heal(Image image,int Xposition, int Yposition, int sizeX, int sizeY) {
        super("Heal", image, Xposition, Yposition, sizeX, sizeY);
        this.hpHeal = 20;
    }
    /**
     * Checks if the Heal item is craftable.
     * @return Always returns false since Heal items are not craftable.
     */
    @Override
    public boolean isCraftable() {
        return false;
    }
    /**
     * Checks if the Heal item is collidable.
     * @return Always returns false since Heal items are not collidable.
     */
    @Override
    public boolean isCollidable() {
        return false;
    }
    /**
     * Handles the interaction of the player with the Heal item.
     * When the player interacts with the Heal item, it gets picked up, becomes invisible, and plays an action sound.
     * @param player The player interacting with the Heal item.
     */
    @Override
    public void interact(Player player) {
        player.pickItem(this);
        setVisible(false);
        player.getAudioManager().playActionSound();
    }
    /**
     * Uses the Heal item to restore the player's health.
     * @param player The player using the Heal item.
     */
    public void useHeal(Player player){
        player.useItem("Heal");
        helpPlayer(player);
    }
    /**
     * Helps the player by restoring their health using the Heal item.
     * @param player The player being helped.
     */
    private void helpPlayer(Player player){
        int playerHealth = player.getHealth();
        int maxHealth = 100;
        if(playerHealth < maxHealth){
            int healthToAdd = Math.min(hpHeal, maxHealth - playerHealth);
            player.setHealth(playerHealth + healthToAdd);
            playerHealth = player.getHealth();
        }
    }
    @Override
    public boolean isInteractable() {
        return true;
    }
    @Override
    public boolean isCollectible() {
        return true;
    }
}
