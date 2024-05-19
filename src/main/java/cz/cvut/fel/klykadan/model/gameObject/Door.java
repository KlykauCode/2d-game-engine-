package cz.cvut.fel.klykadan.model.gameObject;

import cz.cvut.fel.klykadan.model.gameObject.characters.Player;
import cz.cvut.fel.klykadan.model.gameObject.items.Item;
import javafx.scene.image.Image;
import java.util.logging.Logger;
import java.util.logging.Level;
/**
 * Represents a door object in the game world.
 * Extends the GameObject class.
 */

public class Door extends GameObject {
    private static final Logger LOGGER = Logger.getLogger(Door.class.getName());
    protected boolean isLocked;
    /**
     * Constructs a Door object with default image and position.
     * @param Xposition The X position of the door.
     * @param Yposition The Y position of the door.
     * @param sizeX The width of the door.
     * @param sizeY The height of the door.
     * @param isLocked Flag indicating whether the door is initially locked.
     */

    public Door(int Xposition, int Yposition, int sizeX, int sizeY, boolean isLocked){
        this(new Image("/textures/doors/door_red.png"), Xposition, Yposition, sizeX, sizeY, isLocked);
    }

    /**
     * Constructs a Door object with custom image and position.
     * @param image The image representing the door.
     * @param Xposition The X position of the door.
     * @param Yposition The Y position of the door.
     * @param sizeX The width of the door.
     * @param sizeY The height of the door.
     * @param isLocked Flag indicating whether the door is initially locked.
     */
    public Door(Image image, int Xposition, int Yposition, int sizeX, int sizeY,  boolean isLocked) {
        super("Door", image, Xposition, Yposition,  sizeX, sizeY);
        this.isLocked = isLocked;
        this.collision = isLocked;
    }

    /**
     * Handles the interaction with the door.
     * If the player has a key, the door is unlocked and becomes invisible.
     * If the player does not have a key, appropriate action is taken.
     * @param player The player interacting with the door.
     */

    @Override
    public void interact(Player player) {
        if (player.hasItem("Key")) {
            player.useItem("Key");
            player.getAudioManager().playOpenDoorSound();
            this.unlock();
            setVisible(false);
        } else {
            locked(player);
        }
    }
    /**
     * Updates the state of the door.
     * Currently does nothing.
     */
    public void update(){

    }
    private void locked(Player player){
        if(isLocked){
            player.getAudioManager().playClosedDoorSound();
            LOGGER.warning("Player does not have a key.");
        } else {
            LOGGER.info("Door is already open.");
        }
    }

    /**
     * Unlocks the door.
     * Sets the isLocked flag to false and makes the door non-collidable.
     */
    public void unlock() {
        LOGGER.fine("Unlocking the door...");
        this.isLocked = false;
        this.collision = false;
        LOGGER.info("Door unlocked. Collidable now: " + isCollidable());
    }

    @Override
    public boolean isCollidable() {
        return isLocked;
    }
    @Override
    public boolean isInteractable() {
        return true;
    }
}
