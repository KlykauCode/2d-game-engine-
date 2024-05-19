package cz.cvut.fel.klykadan.model.gameObject.characters;

import cz.cvut.fel.klykadan.controller.*;
import cz.cvut.fel.klykadan.model.gameObject.CollisionBox;
import cz.cvut.fel.klykadan.view.GUICoinfig;
import javafx.application.Platform;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/**
 * Represents an enemy character in the game.
 */
public class Enemy extends Character implements Animatable, Collidable, Interactable, Runnable{
    private int damage;
    private boolean isMoving;
    private boolean running = true;
    private Thread thread;
    private int attackCooldown = 30;
    private int currentFrame = 0;
    /**
     * Constructs an enemy character.
     * @param controller The game controller.
     * @param cn GUI configuration.
     * @param textureM Texture manager.
     * @param xPosition Initial X position of the enemy.
     * @param yPosition Initial Y position of the enemy.
     * @param speed Speed of the enemy.
     * @param health Health of the enemy.
     */
    public Enemy(GameController controller, GUICoinfig cn, TextureManager textureM, int xPosition, int yPosition, int speed, int health){
        super(controller, "Enemy", null, cn, textureM, xPosition, yPosition,cn.getTileSize(),cn.getTileSize(),speed,health);
        isMoving = true;
        this.damage = 10;
        loadImages();
        this.sprite = new Sprite(this, textureM, controller);

        thread = new Thread(this);
        thread.start();
    }
    /**
     * Runs the enemy behavior loop.
     */
    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
            update();
        }
    }

    /**
     * Updates the enemy behavior.
     */
    @Override
    public void update(){
        if(this.health > 0){
            super.update();
            attackPlayer(controller.getPlayer());
        } else {
            System.out.println("Killed ");
            isVisible = false;
            running = false;
        }
    }
    /**
     * Performs the action of the enemy.
     */
    @Override
    public void doAction(){
        if (isMoving && FPClocker++ >= 30) {
            FPClocker = 0;
            Random random = new Random();
            int randInt = random.nextInt(4);
            direction = Direction.values()[randInt];
        }
    }
    /**
     * Stops the enemy thread.
     */
    public void stopThread() {
        running = false;
        if (thread != null) {
            thread.interrupt();
        }
    }
    /**
     * Attacks the player if within range.
     * @param player The player character.
     */
    public void attackPlayer(Player player) {
        if (player == null) {
            System.out.println("Player object is not initialized.");
            return;
        }
        int dx = player.getX() - this.getX();
        int dy = player.getY() - this.getY();

        double distance = Math.sqrt(dx * dx + dy * dy);
        double runRange = 5 * cn.getTileSize();
        double attackRange = 0.8 * cn.getTileSize();

        if (distance <= runRange) {
            updateDirectionTowardsPlayer(player);

            if (distance <= attackRange) {
                currentFrame++;

                if (currentFrame >= attackCooldown) {
                    Platform.runLater(() -> {
                        player.setHealth(player.getHealth() - damage);
                        player.setHealth(player.getHealth() - damage);
                        controller.getUI().updateHealth(player.getHealth());
                    });
                    currentFrame = 0;
                }
            } else {
                currentFrame = 0;
            }
        }
    }
    /**
     * Loads the images for the enemy.
     */

    private void loadImages() {
        try {
            upImages = new Image[] {
                    new Image("/enemies/sprites/up1.png"),
                    new Image("/enemies/sprites/up2.png")
            };
            downImages = new Image[] {
                    new Image("/enemies/sprites/down1.png"),
                    new Image("/enemies/sprites/down2.png")
            };
            leftImages = new Image[] {
                    new Image("/enemies/sprites/left1.png"),
                    new Image("/enemies/sprites/left2.png")
            };
            rightImages = new Image[] {
                    new Image("/enemies/sprites/right1.png"),
                    new Image("/enemies/sprites/right2.png")
            };

            upStand = new Image("/enemies/sprites/upStand.png");
            downStand = new Image("/enemies/sprites/downStand.png");
            leftStand = new Image("/enemies/sprites/leftStand.png");
            rightStand = new Image("/enemies/sprites/rightStand.png");
        } catch (Exception e){
            System.err.println("Image not found");
        }
    }

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
    @Override
    public boolean isMoving() {
        return isMoving;
    }
    @Override
    public boolean isInteractable() {
        return false;
    }
    public Sprite getSprite(){ return sprite; }

}
