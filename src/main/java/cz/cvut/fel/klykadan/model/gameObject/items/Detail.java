package cz.cvut.fel.klykadan.model.gameObject.items;

import cz.cvut.fel.klykadan.model.gameObject.characters.Player;
import javafx.scene.image.Image;
/**
 * Represents a detail item that can be used for crafting.
 * This class extends the Item class.
 */
public class Detail extends Item{
    /**
     * Constructs a Detail object with the specified initial position and size.
     * @param initialX The initial X position of the Detail item.
     * @param initialY The initial Y position of the Detail item.
     * @param sizeX The size of the Detail item along the X axis.
     * @param sizeY The size of the Detail item along the Y axis.
     */
    public Detail(int initialX, int initialY, int sizeX, int sizeY) {
        this(new Image("/textures/detail/detail.png"), initialX, initialY, sizeX, sizeY);
    }
    /**
     * Constructs a Detail object with the specified image, position, and size.
     * @param image The image of the Detail item.
     * @param initialX The X position of the Detail item.
     * @param initialY The Y position of the Detail item.
     * @param sizeX The size of the Detail item along the X axis.
     * @param sizeY The size of the Detail item along the Y axis.
     */
    public Detail(Image image, int initialX, int initialY, int sizeX, int sizeY) {
        super("Detail", image, initialX, initialY, sizeX, sizeY);
    }
    /**
     * Checks if the Detail item is craftable.
     * @return Always returns true since Detail items are craftable.
     */
    @Override
    public boolean isCraftable() {
        return true;
    }
    @Override
    public boolean isCollidable() {
        return false;
    }

    /**
     * Handles the interaction of the player with the Detail item.
     * When the player interacts with the Detail item, it gets picked up, becomes invisible, and plays an action sound.
     * @param player The player interacting with the Detail item.
     */
    @Override
    public void interact(Player player) {
        player.pickItem(this);
        setVisible(false);
        player.getAudioManager().playActionSound();
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
