package cz.cvut.fel.klykadan.model.gameObject.characters;

import cz.cvut.fel.klykadan.controller.*;
import cz.cvut.fel.klykadan.model.gameObject.CollisionBox;
import cz.cvut.fel.klykadan.model.gameObject.GameObject;
import cz.cvut.fel.klykadan.view.GUICoinfig;
import javafx.scene.image.Image;

import java.util.Random;
/**
 * Class representing a character in the game.
 * Inherits from the GameObject class and implements the Animatable and Collidable interfaces.
 */
public class Character extends GameObject implements Animatable, Collidable{
    protected int speed;
    protected Direction direction;
    protected GameController controller;
    protected GUICoinfig cn;
    protected TextureManager textureM;
    protected Sprite sprite;
    protected CollisionBox collisionBox;
    protected int health;
    protected Image[] upImages, downImages, leftImages, rightImages;
    protected Image upStand, downStand, leftStand, rightStand;
    protected int FPClocker = 0;
    /**
     * Constructor for the Character class.
     * @param controller The game controller.
     * @param name The name of the character.
     * @param image The image of the character.
     * @param cn The GUI configuration.
     * @param textureM The texture manager.
     * @param sizeX The width of the character.
     * @param sizeY The height of the character.
     * @param Xposition The initial position on the X-axis.
     * @param Yposition The initial position on the Y-axis.
     * @param speed The speed of character movement.
     * @param health The health of the character.
     */
    public Character(GameController controller, String name, Image image, GUICoinfig cn, TextureManager textureM, int sizeX, int sizeY, int Xposition, int Yposition, int speed, int health) {
        super(name, image, sizeX, sizeY, Xposition, Yposition);
        this.speed = speed;
        this.cn = cn;
        this.textureM = textureM;
        this.controller = controller;
        direction = direction.DOWN;
        this.health = health;
        updateCollisionBox();
    }
    /**
     * Method to update the character's collision box.
     */
    public void updateCollisionBox() {
        int boxPositionX = this.getX() + 15;
        int boxPositionY = this.getY() + 25;
        int boxSizeX = this.cn.getTileSize() - 30;
        int boxSizeY = this.cn.getTileSize() - 30;

        this.collisionBox = new CollisionBox(boxPositionX, boxPositionY, boxSizeX, boxSizeY);
    }
    /**
     * Method for the character's action.
     * Overridden in subclasses.
     */
    public void doAction(){

    }
    /**
     * Method to update the character's state.
     */
    public void update(){
        doAction();
        setCollision(false);
        controller.getPhysics().detectCollision(this);
        if(!isCollision()){
            switch (direction) {
                case UP: Yposition -= speed; break;
                case DOWN: Yposition += speed; break;
                case LEFT: Xposition -= speed; break;
                case RIGHT: Xposition += speed; break;
            }
            updateCollisionBox();
        }
        sprite.updateSprite(isMoving(), false);
    }
    /**
     * Method to get an array of images for movement animation.
     * @return An array of images for movement animation.
     */
    @Override
    public Image[] getMovementImages() {
        switch (direction) {
            case UP: return upImages;
            case DOWN: return downImages;
            case LEFT: return leftImages;
            case RIGHT: return rightImages;
            default: return new Image[] {downStand};
        }
    }
    /**
     * Method to get the standing image of the character.
     * @return The standing image of the character.
     */
    @Override
    public Image getStandingImage() {
        switch (direction) {
            case UP: return upStand;
            case DOWN: return downStand;
            case LEFT: return leftStand;
            case RIGHT: return rightStand;
            default: return downStand;
        }
    }
    /**
     * Updates the character's direction to face the player.
     * @param player The player character.
     */
    public void updateDirectionTowardsPlayer(Player player) {
        int dx = player.getX() - this.getX();
        int dy = player.getY() - this.getY();

        if (Math.abs(dx) > Math.abs(dy)) {
            if (dx > 0) {
                this.direction = Direction.RIGHT;
            } else {
                this.direction = Direction.LEFT;
            }
        } else {
            if (dy > 0) {
                this.direction = Direction.DOWN;
            } else {
                this.direction =  Direction.UP;
            }
        }
    }
    @Override
    public boolean isCollidable() {
        return true;
    }

    public int getX() {
        return Xposition;
    }
    public int getY() { return Yposition; }
    public int getScreenX(){
        return screenPositionX;
    }
    public int getScreenY(){
        return screenPositionY;
    }
    @Override
    public boolean isMoving() {
        return false;
    }
    public Direction getDirection() {
        return direction;
    }
    public boolean isCollision(){ return collision; }
    public int getSpeed(){ return speed; }
    public CollisionBox getCollisionBox() {
        return collisionBox;
    }
    public void setCollision(boolean collision) {
        this.collision = collision;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getHealth() {
        return health;
    }
}
