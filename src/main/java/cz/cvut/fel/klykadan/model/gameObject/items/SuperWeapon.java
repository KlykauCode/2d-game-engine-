package cz.cvut.fel.klykadan.model.gameObject.items;

import cz.cvut.fel.klykadan.controller.Direction;
import cz.cvut.fel.klykadan.controller.GameController;
import cz.cvut.fel.klykadan.model.gameObject.Bullet;
import cz.cvut.fel.klykadan.model.gameObject.characters.Player;
import cz.cvut.fel.klykadan.view.GUICoinfig;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;
/**
 * A class representing a SuperWeapon item in the game.
 */
public class SuperWeapon extends Item{
    private GUICoinfig cn;
    private List<Bullet> bullets;
    private int ammoCount;
    private int fireRate = 0;
    /**
     * Constructs a SuperWeapon object with the specified GUI configuration.
     * @param cn The GUI configuration object.
     */
    public SuperWeapon(GUICoinfig cn) {
        super("SuperWeapon", new Image("/textures/weapon/gun3.png"), 0,0,0,0);
        this.cn = cn;
        this.bullets = new ArrayList<>();
        this.ammoCount = 150;

        setVisible(true);
    }
    /**
     * Performs an attack action with the super weapon.
     * @param player The player performing the attack.
     * @param controller The game controller managing the game.
     */
    public void attack(Player player, GameController controller){
        if(ammoCount > 0){
            if(fireRate++ >= 2){
                fireRate = 0;
                controller.getAudioManager().playShootSound();
                int bulletX = player.getXposition();
                int bulletY = player.getYposition();
                Direction direction = player.getDirection();

                Bullet bullet = new Bullet(bulletX, bulletY, direction, cn, controller);
                controller.getGameObjects().add(bullet);
                bullets.add(bullet);
                ammoCount--;

                System.out.println("Ammo count is: " + ammoCount);
            }
        } else {
            controller.getAudioManager().playEmptySound();
            System.out.println("No ammo.");
        }
    }
    /**
     * Reloads the super weapon with the specified amount of ammunition.
     * @param amount The amount of ammunition to reload.
     */
    public void reload(int amount) {
        ammoCount += amount;
        System.out.println("Reloaded to " + ammoCount + " bullets");
    }
    public int getAmmoCount() {
        return ammoCount;
    }

    public void setAmmoCount(int ammoCount) {
        this.ammoCount = ammoCount;
    }

    @Override
    public boolean isCraftable() {
        return true;
    }

}
