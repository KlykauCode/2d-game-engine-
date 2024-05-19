package cz.cvut.fel.klykadan.model.gameObject;

import cz.cvut.fel.klykadan.controller.GameController;
import cz.cvut.fel.klykadan.model.gameObject.characters.Player;
import javafx.scene.image.Image;

import java.util.logging.Logger;
/**
 * Represents a special door object that transitions to the next level when interacted with.
 * Extends the Door class.
 */
public class LevelDoor extends Door {
    private static final Logger LOGGER = Logger.getLogger(LevelDoor.class.getName());
    /**
     * Constructs a LevelDoor object with default image and position.
     * @param Xposition The X position of the level door.
     * @param Yposition The Y position of the level door.
     * @param sizeX The width of the level door.
     * @param sizeY The height of the level door.
     * @param isLocked Flag indicating whether the level door is initially locked.
     */
    public LevelDoor(int Xposition, int Yposition, int sizeX, int sizeY, boolean isLocked) {
        super(new Image("/textures/lab/tile050.png"), Xposition, Yposition, sizeX, sizeY, isLocked);
        this.name = "LevelDoor";
    }

    /**
     * Handles the interaction with the level door.
     * If the player has a level key, the next level is started.
     * If the player does not have a level key, appropriate action is taken.
     * @param player The player interacting with the level door.
     */
    @Override
    public void interact(Player player) {
        if (player.hasItem("LevelKey")) {
            player.useItem("LevelKey");
            startNextLevel(player);
        } else {
            LOGGER.warning("Need a special key to open the door!");
            player.getAudioManager().playClosedDoorSound();
        }
    }
    private void startNextLevel(Player player){
        player.getController().switchToNextLevel(player.getCurrentLevel());
    }
}
