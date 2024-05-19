package cz.cvut.fel.klykadan.model.gameObject;

import cz.cvut.fel.klykadan.controller.Direction;
import cz.cvut.fel.klykadan.controller.GameController;
import cz.cvut.fel.klykadan.model.gameObject.characters.Enemy;
import cz.cvut.fel.klykadan.model.gameObject.characters.Player;
import cz.cvut.fel.klykadan.view.GUICoinfig;
import javafx.scene.image.Image;
/**
 * Represents a bullet in the game, handling its movement, collision detection, and impact.
 * A bullet can move in a specified direction, cause damage to enemies, and has a limited range after which it disappears.
 */
public class Bullet extends GameObject {
    private int speed;
    private GUICoinfig cn;
    private int damage;
    private Direction direction;
    private GameController controller;
    private final int initialX;
    private final int initialY;
    /**
     * Constructs a bullet with specified starting position, direction, configuration, and game controller.
     *
     * @param initialX the initial x-coordinate of the bullet
     * @param initialY the initial y-coordinate of the bullet
     * @param direction the direction in which the bullet will travel
     * @param cn the configuration settings of the game UI, used to manage game tile sizes and scaling
     * @param controller the game controller, used for accessing game state and objects
     */
    public Bullet(int initialX, int initialY, Direction direction, GUICoinfig cn, GameController controller) {
        super("Bullet", new Image("/textures/weapon/bullet.png"), initialX, initialY,  cn.getTileSize(), cn.getTileSize());
        this.initialX = initialX;
        this.initialY = initialY;
        this.direction = direction;
        this.cn = cn;
        this.controller = controller;
        this.damage = 25;
        this.speed = 35;
        this.isVisible = true;
    }
    /**
     * Updates the position of the bullet based on its speed and direction.
     * Checks for collisions with enemies and applies damage if a collision occurs.
     * The bullet will disappear after traveling a certain distance or upon colliding with an enemy.
     */
    @Override
    public void update() {
        switch (direction) {
            case UP: Yposition -= speed; break;
            case DOWN: Yposition += speed; break;
            case LEFT: Xposition -= speed; break;
            case RIGHT: Xposition += speed; break;
        }

        double distanceTraveled = Math.sqrt(Math.pow(Xposition - initialX, 2) + Math.pow(Yposition - initialY, 2));
        if (distanceTraveled >= 10 * cn.getTileSize()) {
            isVisible = false;
        }

        for (Enemy enemy : controller.getEnemies()) {
            if (isCollidingWith(enemy)) {
                enemy.setHealth(enemy.getHealth() - damage);
                isVisible = false;
                break;
            }
        }
    }
    /**
     * Checks if the bullet is colliding with a specified enemy.
     * Collision is determined based on the proximity of the bullet to the enemy.
     *
     * @param enemy the enemy to check for collision with
     * @return true if the bullet is colliding with the enemy, false otherwise
     */
    private boolean isCollidingWith(Enemy enemy) {
        int distanceX = Math.abs(enemy.getX() - this.getXposition());
        int distanceY = Math.abs(enemy.getY() - this.getYposition());
        return distanceX <= cn.getTileSize() && distanceY <= cn.getTileSize();
    }
}

