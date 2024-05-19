package cz.cvut.fel.klykadan.model.gameObject.items;

import cz.cvut.fel.klykadan.controller.Direction;
import cz.cvut.fel.klykadan.controller.GameController;
import cz.cvut.fel.klykadan.model.gameObject.Bullet;
import cz.cvut.fel.klykadan.model.gameObject.characters.Player;
import cz.cvut.fel.klykadan.view.GUICoinfig;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

public class Weapon extends Item {
    private GUICoinfig cn;
    private List<Bullet> bullets;
    private int ammoCount;
    private double fireRate;
    private static final Logger LOGGER = Logger.getLogger(Weapon.class.getName());
    /**
     * Constructs a Weapon object with default image and parameters.
     * @param cn The GUI configuration object.
     * @param initialX The initial X position of the weapon.
     * @param initialY The initial Y position of the weapon.
     * @param sizeX The width of the weapon.
     * @param sizeY The height of the weapon.
     */
    public Weapon(GUICoinfig cn, int initialX, int initialY, int sizeX, int sizeY) {
        this(new Image("/textures/weapon/gun2.png"), cn, initialX, initialY, sizeX, sizeY);
        this.fireRate = 4;
    }

    /**
     * Constructs a Weapon object with specified image and parameters.
     * @param image The image representing the weapon.
     * @param cn The GUI configuration object.
     * @param initialX The initial X position of the weapon.
     * @param initialY The initial Y position of the weapon.
     * @param sizeX The width of the weapon.
     * @param sizeY The height of the weapon.
     */
    public Weapon(Image image, GUICoinfig cn, int initialX, int initialY, int sizeX, int sizeY) {
        super("Gun", image, initialX, initialY, sizeX, sizeY);
        this.cn = cn;
        this.bullets = new ArrayList<>();
        this.ammoCount = 15;

        setVisible(true);
    }
    /**
     * Attacks with the weapon, spawning bullets and reducing ammo count.
     * @param player The player performing the attack.
     * @param controller The game controller managing game objects.
     */
    public void attack(Player player, GameController controller){
        if(ammoCount > 0){
            if(fireRate++ >= 10){
                fireRate = 0;
                controller.getAudioManager().playShootSound();
                int bulletX = player.getXposition();
                int bulletY = player.getYposition();
                Direction direction = player.getDirection();

                Bullet bullet = new Bullet(bulletX, bulletY, direction, cn, controller);
                controller.getGameObjects().add(bullet);
                bullets.add(bullet);
                ammoCount--;
                LOGGER.info("Ammo count is: " + ammoCount);
            }
        } else {
            controller.getAudioManager().playEmptySound();
            LOGGER.warning("No ammo.");
        }
    }
    /**
     * Reloads the weapon with the specified amount of ammo.
     * @param amount The amount of ammo to reload.
     */
    public void reload(int amount) {
        ammoCount += amount;
        LOGGER.info("Reloaded to " + ammoCount + " bullets");
    }
    @Override
    public boolean isCraftable() {
        return true;
    }
    @Override
    public boolean isCollidable() {
        return false;
    }
    /**
     * Handles interaction with the player when the player interacts with the weapon.
     * Picks up the weapon, makes it invisible, and plays an action sound.
     * @param player The player interacting with the weapon.
     */
    @Override
    public void interact(Player player) {
        player.pickItem(this);
        setVisible(false);
        player.getAudioManager().playActionSound();
        LOGGER.info("Picked up weapon. Current ammo count: " + ammoCount);
    }
    @Override
    public boolean isInteractable() {
        return true;
    }
    @Override
    public boolean isCollectible() {
        return true;
    }

    public int getAmmoCount() {
        return ammoCount;
    }

    public void setAmmoCount(int ammoCount) {
        this.ammoCount = ammoCount;
    }
}

