package cz.cvut.fel.klykadan.model.gameObject.items;

import cz.cvut.fel.klykadan.controller.GameController;
import cz.cvut.fel.klykadan.model.gameObject.CollisionBox;
import cz.cvut.fel.klykadan.model.gameObject.characters.Player;
import cz.cvut.fel.klykadan.view.GUICoinfig;
import javafx.scene.image.Image;
/**
 * A class representing a generic Key item in the game, extending the Item class.
 */
public class Key extends Item{
    private boolean used;
    private GUICoinfig cn;

    /**
     * Constructs a Key object with the specified initial position and size.
     * @param initialX The initial X position of the key.
     * @param initialY The initial Y position of the key.
     * @param sizeX The size of the key along the X axis.
     * @param sizeY The size of the key along the Y axis.
     */

    public Key(int initialX, int initialY, int sizeX, int sizeY) {
        this(new Image("/textures/keys/doorKey.png"), initialX, initialY, sizeX, sizeY);
    }

    /**
     * Constructs a Key object with the specified image, initial position, and size.
     * @param image The image representing the key.
     * @param initialX The initial X position of the key.
     * @param initialY The initial Y position of the key.
     * @param sizeX The size of the key along the X axis.
     * @param sizeY The size of the key along the Y axis.
     */
    public Key(Image image, int initialX, int initialY, int sizeX, int sizeY) {
        super("Key", image, initialX, initialY, sizeX, sizeY);
        this.used = false;
        this.collision = false;
    }
    @Override
    public boolean isCraftable() {
        return false;
    }
    @Override
    public boolean isCollidable() {
        return false;
    }

    @Override
    public void interact(Player player) {
        player.pickItem(this);
        this.used = true;
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
