package cz.cvut.fel.klykadan.model.gameObject;
/**
 * Represents a collision box used for detecting and managing collisions in the game.
 * A collision box is defined by its size (width and height) and position (x and y coordinates).
 */
public class CollisionBox{
    private int sizeX, sizeY;
    private int xPosition, yPosition;

    /**
     * Constructs a CollisionBox with specified dimensions and initial position.
     *
     * @param xPosition the initial x-coordinate of the collision box
     * @param yPosition the initial y-coordinate of the collision box
     * @param sizeX the width of the collision box
     * @param sizeY the height of the collision box
     */

    public CollisionBox(int xPosition, int yPosition, int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }
    /**
     * Updates the position of the collision box.
     * This method is typically called to move the collision box in synchronization with the associated entity.
     *
     * @param newX the new x-coordinate of the collision box
     * @param newY the new y-coordinate of the collision box
     */
    public void update(int newX, int newY) {
        this.xPosition = newX;
        this.yPosition = newY;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public int getxPosition() {
        return xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

}
