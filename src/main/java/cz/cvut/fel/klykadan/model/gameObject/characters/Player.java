package cz.cvut.fel.klykadan.model.gameObject.characters;

import cz.cvut.fel.klykadan.controller.*;
import cz.cvut.fel.klykadan.model.gameObject.Bullet;
import cz.cvut.fel.klykadan.model.gameObject.GameObject;
import cz.cvut.fel.klykadan.model.gameObject.items.*;
import cz.cvut.fel.klykadan.view.GUICoinfig;
import cz.cvut.fel.klykadan.view.UI;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
/**
 * Represents the player character in the game.
 * This class extends the Character class and implements the Animatable interface.
 */
public class Player extends Character implements Animatable {
    private InputHandler input;
    private Inventory inventory;
    private int maxHealth;
    private GraphicsContext gc;
    private Weapon weapon;
    private SuperWeapon superWeapon;
    private AudioManager audioManager;
    private int stepSoundLocker = 0;
    private int defaultSteps = 12;
    private int steps = defaultSteps;
    private int currentLevel;
    private static final Logger LOGGER = Logger.getLogger(Player.class.getName());
    /**
     * Constructs a Player object with the specified parameters.
     * @param controller The game controller.
     * @param input The input handler for the player.
     * @param cn The GUI configuration.
     * @param textureM The texture manager.
     * @param audioManager The audio manager.
     */

    public Player(GameController controller, InputHandler input, GUICoinfig cn, TextureManager textureM, AudioManager audioManager) {
        super(controller,"Player", null, cn, textureM, cn.getTileSize(), cn.getTileSize(), 0, 0, 5, 100);
        this.input = input;
        this.maxHealth = health;
        this.audioManager = audioManager;
        this.inventory = new Inventory();
        currentLevel = 1;

        setDefaultValues();
        loadImages();

        this.sprite = new Sprite(this, textureM, controller);
    }
    /**
     * Resets the player's state to its initial values.
     */
    public void reset() {
        setHealth(maxHealth);
        inventory.clear();
        inventory.clearRemovedItems();
        setDefaultValues();
        currentLevel = 1;
        setCurrentLevel(currentLevel);
        for (FriendlyNPC npc : controller.getNpcs()) {
            if (npc != null) {
                npc.stopInteraction();
            }
        }
        direction = Direction.DOWN;
    }
    /**
     * Sets the default values for player position and screen position.
     */
    private void setDefaultValues(){
        screenPositionX = cn.getScreenWidth()/2 - (cn.getTileSize()/2);
        screenPositionY = cn.getScreenHeight()/2 - (cn.getTileSize()/2);
        Xposition = cn.getTileSize() * 6;
        Yposition = cn.getTileSize() * 35;
    }

    /**
     * Loads images for player animations.
     */

    private void loadImages() {
        upImages = new Image[] {
                new Image("/player/up1.png"),
                new Image("/player/up2.png")
        };
        downImages = new Image[] {
                new Image("/player/down1.png"),
                new Image("/player/down2.png")
        };
        leftImages = new Image[] {
                new Image("/player/left1.png"),
                new Image("/player/left2.png")
        };
        rightImages = new Image[] {
                new Image("/player/right1.png"),
                new Image("/player/right2.png")
        };

        upStand = new Image("/player/upStand.png");
        downStand = new Image("/player/downStand.png");
        leftStand = new Image("/player/leftStand.png");
        rightStand = new Image("/player/rightStand.png");
    }
    /**
     * Updates the player's state.
     */

    public void updatePlayer(){
        currentLevel = getCurrentLevel();
        boolean isMoving = input.isMoving();
        sprite.updateSprite(isMoving, input.isShiftPressed());
        controller.getUI().updateWeaponUI();
        int actualSpeed = speed;

        if(input.isShiftPressed()){
            actualSpeed += 2;
            steps = defaultSteps - 5;
        } else {
            steps = defaultSteps;
        }
        if(input.isEPressed()){
            interactWithObjects();
            input.setEIsPressed(false);
        }
        if(input.isSpacePressed()){
            attack();
        }
        if(input.ishIsPressed()){
            if(inventory.hasItem("Heal")){
                Item heal = inventory.getItem("Heal");
                if (heal instanceof Heal) {
                    ((Heal) heal).useHeal(this);
                }
            }
        }
        if(input.isrIsPressed()){
            weapon = getWeapon();
            superWeapon = getSuperWeapon();
            if(inventory.hasItem("Ammo")){
                Item ammo = inventory.getItem("Ammo");
                if (ammo instanceof Ammo) {
                    ((Ammo) ammo).reloadWeapon(weapon, superWeapon, this);
                }
            }
        }
        if(input.isMoving()) {
            if(stepSoundLocker++ >= steps){
                stepSoundLocker = 0;
                controller.getAudioManager().playMoveSound();
            }
            if (input.isUpPressed()) {
                direction = Direction.UP;
            } else if (input.isDownPressed()) {
                direction = Direction.DOWN;
            } else if (input.isLeftPressed()) {
                direction = Direction.LEFT;
            } else if (input.isRightPressed()) {
                direction = Direction.RIGHT;
            }

            setCollision(false);
            controller.getPhysics().detectCollision(this);
            if(!isCollision()){
                switch (direction) {
                    case UP: Yposition -= actualSpeed; break;
                    case DOWN: Yposition += actualSpeed; break;
                    case LEFT: Xposition -= actualSpeed; break;
                    case RIGHT: Xposition += actualSpeed; break;
                }
            }
        }
        for(FriendlyNPC npc : controller.getNpcs()){
            if (npc != null && !isInteractDistance(npc)) {
                npc.stopInteraction();
            }
        }
        if(this.health <= 0){
            controller.getStateController().setCurrentState(GameStateController.State.GAME_OVER);
        }
    }
    /**
     * Handles player's attack action.
     */
    private void attack() {
        weapon = getWeapon();
        superWeapon = getSuperWeapon();
        if (inventory.hasItem("Gun")) {
            weapon.attack(this, controller);
        } else if (inventory.hasItem("SuperWeapon")){
            superWeapon.attack(this, controller);
        } else {
            LOGGER.warning("Nothing to attack with");
        }
    }

    /**
     * Interacts with objects in the game world.
     */
    private void interactWithObjects(){
        GameObject nearestObject = null;
        double minDistance = Double.MAX_VALUE;

        List<GameObject> allInteractables = new ArrayList<>(controller.getGameObjects());
        allInteractables.addAll(controller.getNpcs());

        for(GameObject obj : allInteractables){
            if(obj.isInteractable() && isNear(obj)){
                double distance = Math.sqrt(Math.pow(obj.getXposition() - this.getX(),2) +
                        Math.pow(obj.getYposition() - this.getY(),2)); //Euclidean distance between two points

                if(distance < minDistance){
                    minDistance = distance;
                    nearestObject = obj;
                }
            }
        }
        if (nearestObject != null) {
            nearestObject.interact(this);
            if(nearestObject.isCollectible() && hasItem("Gun")){
                controller.removeGameObject(nearestObject);
            }
        } else {
            LOGGER.info("No items to interact with");
        }
    }
    /**
     * Checks if the player is near a game object.
     * @param obj The game object to check.
     * @return True if the player is near the object, false otherwise.
     */
    private boolean isNear(GameObject obj){
        int threshold = cn.getTileSize(); //max distance to interact

        int distanceX = Math.abs(obj.getXposition() - this.getX());
        int distanceY = Math.abs(obj.getYposition() - this.getY());
        boolean isCloseByX = distanceX <= threshold;
        boolean isCloseByY = distanceY <= threshold;
        boolean isNear = isCloseByY && isCloseByX;
        return isNear;
    }
    /**
     * Checks if the player is within interaction distance of a friendly NPC.
     * @param npc The friendly NPC to check.
     * @return True if the player is within interaction distance, false otherwise.
     */
    private boolean isInteractDistance(FriendlyNPC npc){
        int distanceThreshold = cn.getTileSize() * 5;
        double distance = Math.sqrt(Math.pow(npc.getXposition() - this.getX(), 2) + Math.pow(npc.getYposition() - this.getY(), 2));
        return distance <= distanceThreshold;
    }
    /**
     * Picks up an item and adds it to the player's inventory.
     * @param item The item to pick up.
     */
    public void pickItem(Item item){
        inventory.addItem(item);
        LOGGER.info("Picking item: " + item.getName());
    }
    /**
     * Checks if the player has a specific item in the inventory.
     * @param itemName The name of the item to check for.
     * @return True if the player has the item, false otherwise.
     */
    public boolean hasItem(String itemName) {
        return inventory.hasItem(itemName);
    }

    /**
     * Uses a specific item from the inventory.
     * @param itemName The name of the item to use.
     */
    public void useItem(String itemName) {
        inventory.useItem(itemName);
    }
    @Override
    public boolean isCollidable() {
        return true;
    }
    public Sprite getSprite() {
        return sprite;
    }

    public AudioManager getAudioManager() {
        return audioManager;
    }
    public GameController getController(){ return controller; }
    @Override
    public boolean isMoving() {
        if(controller.getStateController().isState(GameStateController.State.PAUSE)){
            return false;
        } else {
            return input.isMoving();
        }
    }
    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
        controller.getUI().updateHealth(this.health);
    }
    public void setWeapon(Weapon weapon){
        this.weapon = weapon;
    }
    public void setSuperWeapon(SuperWeapon superWeapon) {this.superWeapon = superWeapon;}

    public Weapon getWeapon() {
        if (inventory.hasItem("Gun")) {
            Item item = inventory.getItem("Gun");
            if (item instanceof Weapon) {
                return (Weapon) item;
            }
        }
        return null;
    }

    public SuperWeapon getSuperWeapon() {
        if (inventory.hasItem("SuperWeapon")) {
            Item item = inventory.getItem("SuperWeapon");
            if (item instanceof SuperWeapon) {
                return (SuperWeapon) item;
            }
        }
        return null;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setX(int x) {
        this.Xposition = x;
        updateCollisionBox();
    }
    public void setY(int y) {
        this.Yposition = y;
        updateCollisionBox();
    }
    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public GUICoinfig getGUICoinfig() {
        return cn;
    }
}
