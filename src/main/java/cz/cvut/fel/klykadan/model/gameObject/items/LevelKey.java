package cz.cvut.fel.klykadan.model.gameObject.items;

import cz.cvut.fel.klykadan.model.gameObject.characters.Player;
import javafx.application.Platform;
import javafx.scene.image.Image;
/**
 * A class representing a LevelKey item in the game, extending the Key class.
 */
public class LevelKey extends Key {
    /**
     * Constructs a LevelKey object with the specified initial position and size.
     * @param initialX The initial X position of the key.
     * @param initialY The initial Y position of the key.
     * @param sizeX The size of the key along the X axis.
     * @param sizeY The size of the key along the Y axis.
     */
    public LevelKey(int initialX, int initialY, int sizeX, int sizeY) {
        super(new Image("/textures/keys/levelKey.png"), initialX, initialY, sizeX, sizeY);
        this.name = "LevelKey";
    }
    @Override
    public boolean isCraftable() {
        return false;
    }

    @Override
    public void interact(Player player) {
        player.pickItem(this);
        this.setVisible(false);
        player.getAudioManager().playActionSound();

    }
}
