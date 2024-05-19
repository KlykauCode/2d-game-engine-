package cz.cvut.fel.klykadan.model.gameObject.items;

import cz.cvut.fel.klykadan.controller.Interactable;
import cz.cvut.fel.klykadan.model.gameObject.GameObject;
import cz.cvut.fel.klykadan.model.gameObject.characters.Player;
import javafx.scene.image.Image;

import java.util.UUID;
/**
 * A class representing a generic item in the game, extending the GameObject class.
 */
public class Item extends GameObject {
    protected String name;
    protected boolean craftable;
    private Image image;
    /**
     * Constructs an Item object with the specified name, image, position, and size.
     * @param name The name of the item.
     * @param image The image representing the item.
     * @param Xposition The X position of the item.
     * @param Yposition The Y position of the item.
     * @param sizeX The size of the item along the X axis.
     * @param sizeY The size of the item along the Y axis.
     */
    public Item(String name, Image image, int Xposition, int Yposition, int sizeX, int sizeY) {
        super(name, image, Xposition, Yposition, sizeX, sizeY);
        this.name = name;
        this.image = image;
        this.craftable = craftable;
    }
    /**
     * Checks if the item is craftable.
     * @return True if the item is craftable, false otherwise.
     */
    public boolean isCraftable() {
        return craftable;
    }
    public void update(){

    }
    public String getName() {
        return name;
    }

}
