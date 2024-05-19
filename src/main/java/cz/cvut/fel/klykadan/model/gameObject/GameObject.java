package cz.cvut.fel.klykadan.model.gameObject;

import cz.cvut.fel.klykadan.controller.Collidable;
import cz.cvut.fel.klykadan.controller.GameController;
import cz.cvut.fel.klykadan.controller.Interactable;
import cz.cvut.fel.klykadan.model.gameObject.characters.Player;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.UUID;

/**
 * Represents a game object that can be interacted with and collided with in the game world.
 * Provides basic properties and functionality for game objects.
 */
public abstract class GameObject implements Collidable, Interactable {

    protected String name;
    protected CollisionBox collisionBox;

    protected Image image;

    protected int sizeX;
    protected int sizeY;
    protected int Xposition;
    protected int Yposition;
    protected int screenPositionX;
    protected int screenPositionY;
    protected boolean collision;
    protected boolean isVisible;
    private UUID id;
    /**
     * Constructs a game object with the specified properties.
     * @param name The name of the game object.
     * @param image The image representing the game object.
     * @param Xposition The X position of the game object.
     * @param Yposition The Y position of the game object.
     * @param sizeX The width of the game object.
     * @param sizeY The height of the game object.
     */
    public GameObject(String name, Image image, int Xposition, int Yposition, int sizeX, int sizeY) {
        this.name = name;
        this.image = image;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.Xposition = Xposition;
        this.Yposition = Yposition;
        this.collisionBox = new CollisionBox(Xposition, Yposition, sizeX, sizeY);
        this.collision = false;
        this.isVisible = true;
        this.id = UUID.randomUUID();
    }
    /**
     * Updates the state of the game object.
     * This method should be implemented by subclasses to define specific update behavior.
     */
    public abstract void update();

    @Override
    public CollisionBox getCollisionBox() {
        return collisionBox;
    }

    @Override
    public boolean isCollidable() {
        return true;
    }

    @Override
    public void setCollision(boolean hasCollided) {
        this.collision = hasCollided;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public int getXposition() {
        return Xposition;
    }

    public int getYposition() {
        return Yposition;
    }

    public boolean isCollision() {
        return collision;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public void setVisible(boolean visible) {
        this.isVisible = visible;
    }

    public boolean isVisible() {
        return isVisible;
    }

    @Override
    public void interact(Player player) {

    }

    @Override
    public boolean isInteractable() {
        return false;
    }

    @Override
    public boolean isCollectible() {
        return false;
    }

    public String getName() {
        return name;
    }
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
