package cz.cvut.fel.klykadan.controller;

import cz.cvut.fel.klykadan.model.gameObject.CollisionBox;
import cz.cvut.fel.klykadan.model.gameObject.GameObject;
import cz.cvut.fel.klykadan.model.gameObject.characters.Character;
import cz.cvut.fel.klykadan.model.gameObject.characters.Player;
import cz.cvut.fel.klykadan.view.GUICoinfig;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
/**
 * The Sprite class manages drawing and animation for entities in the game that are capable of animation.
 * It handles the positioning and rendering of entities based on their state and location in the game world.
 */
public class Sprite {

    private Animatable entity;
    private TextureManager textureM;
    private GameController controller;
    private int spriteCount = 0;
    private int spriteNum = 0;
    /**
     * Constructs a Sprite for an animatable entity.
     *
     * @param entity the animatable entity this sprite will represent
     * @param textureM the texture manager to retrieve sprite images
     * @param controller the game controller, used for accessing global game state
     */

    public Sprite(Animatable entity, TextureManager textureM, GameController controller) {
        this.entity = entity;
        this.textureM = textureM;
        this.controller = controller;
    }
    /**
     * Draws the entity on the canvas using the provided graphics context.
     * The drawing accounts for special conditions such as being under special tiles that might affect visibility.
     *
     * @param gc the graphics context on which the sprite is drawn
     * @param cn configuration settings of the GUI
     * @param drawX the x-coordinate on the canvas where the sprite should be drawn
     * @param drawY the y-coordinate on the canvas where the sprite should be drawn
     */
    public void drawEntity(GraphicsContext gc, GUICoinfig cn, double drawX, double drawY) {
        int tileX = (entity.getX() + 15) / cn.getTileSize();
        int tileY = (entity.getY() + 25) / cn.getTileSize();
        int tileUnderEntity = textureM.getMap()[tileX][tileY];

        boolean isUnderSpecialTile = tileUnderEntity == 8;

        if (!isUnderSpecialTile){
            Image image = selectImageBasedOnDirectionAndAnimationState();
            gc.drawImage(image, drawX, drawY, cn.getTileSize(), cn.getTileSize());
        }
    }
    /**
     * Draws the player sprite. This method adjusts for the player's position on the screen.
     *
     * @param gc the graphics context on which the player is drawn
     * @param cn GUI configuration settings used for drawing
     */

    public void drawPlayer(GraphicsContext gc, GUICoinfig cn) {
        drawEntity(gc, cn, entity.getScreenX(), entity.getScreenY());
    }
    /**
     * Draws an NPC sprite relative to the player's position on the screen.
     *
     * @param gc the graphics context on which the NPC is drawn
     * @param cn GUI configuration settings used for drawing
     */

    public void drawNPC(GraphicsContext gc, GUICoinfig cn){
        Player player = controller.getPlayer();
        double entityX = entity.getX() - player.getXposition() + player.getScreenX();
        double entityY = entity.getY() - player.getYposition() + player.getScreenY();
        drawEntity(gc, cn, entityX, entityY);
    }

    private Image selectImageBasedOnDirectionAndAnimationState() {
        Image[] movementImages = entity.getMovementImages();
        Image standImage = entity.getStandingImage();

        if (entity.isMoving()) {
            return movementImages[spriteNum % movementImages.length];
        } else {
            return standImage;
        }
    }
    /**
     * Updates the sprite's animation state, advancing the animation frame based on the entity's movement and whether shift is pressed.
     *
     * @param isMoving indicates whether the entity is currently moving
     * @param isShiftPressed indicates whether the shift key is pressed, affecting the animation speed
     */
    public void updateSprite(boolean isMoving, boolean isShiftPressed) {
        if (isMoving) {
            spriteCount++;
            int updateThreshold;
            if (isShiftPressed) {
                updateThreshold = 5;
            } else {
                updateThreshold = 10;
            }
            if (spriteCount > updateThreshold) {
                spriteNum = spriteNum % 2 + 1;
                spriteCount = 0;
            }
        } else {
            spriteNum = 0;
            spriteCount = 0;
        }
    }
}

