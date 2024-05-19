package cz.cvut.fel.klykadan.model.gameObject.items;

import cz.cvut.fel.klykadan.model.gameObject.characters.Player;
import javafx.scene.image.Image;
/**
 * Represents an ammo item that can be used to reload weapons.
 * This class extends the Item class.
 */
public class Ammo extends Item{
    private int count;
    private int superCount;
    /**
     * Constructs an Ammo object with the specified initial position and size.
     * Initializes the regular ammo count to 15 and the super ammo count to 58.
     * @param initialX The initial X position of the Ammo item.
     * @param initialY The initial Y position of the Ammo item.
     * @param sizeX The size of the Ammo item along the X axis.
     * @param sizeY The size of the Ammo item along the Y axis.
     */
    public Ammo(int initialX, int initialY, int sizeX, int sizeY){
        this(new Image("/textures/weapon/ammo.png"), initialX, initialY, sizeX, sizeY);
    }
    /**
     * Constructs an Ammo object with the specified image, position, and size.
     * Initializes the regular ammo count to 15 and the super ammo count to 58.
     * @param image The image of the Ammo item.
     * @param initialX The X position of the Ammo item.
     * @param initialY The Y position of the Ammo item.
     * @param sizeX The size of the Ammo item along the X axis.
     * @param sizeY The size of the Ammo item along the Y axis.
     */
    public Ammo(Image image, int initialX, int initialY, int sizeX, int sizeY) {
        super("Ammo", image, initialX, initialY, sizeX, sizeY);
        this.count = 15;
        this.superCount = 58;
    }

    @Override
    public boolean isCollidable() {
        return false;
    }
    /**
     * Handles the interaction of the player with the Ammo item.
     * When the player interacts with the Ammo item, it gets picked up and becomes invisible.
     * @param player The player interacting with the Ammo item.
     */
    @Override
    public void interact(Player player) {
        player.pickItem(this);
        setVisible(false);

    }
    /**
     * Reloads the weapons of the player with the available ammo counts.
     * After reloading, the player uses up the Ammo item.
     * @param weapon The regular weapon to reload.
     * @param superWeapon The super weapon to reload.
     * @param player The player performing the reload.
     */
    public void reloadWeapon(Weapon weapon, SuperWeapon superWeapon, Player player){
        if (weapon != null) {
            weapon.reload(count);
        }
        if (superWeapon != null) {
            superWeapon.reload(superCount);
        }
        player.useItem("Ammo");
        player.getAudioManager().playReloadSound();
    }
    @Override
    public boolean isCraftable() {
        return false;
    }
    @Override
    public boolean isInteractable() {
        return true;
    }
    @Override
    public boolean isCollectible() {
        return true;
    }
}
