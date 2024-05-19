package cz.cvut.fel.klykadan.model.gameObject.characters;

import cz.cvut.fel.klykadan.controller.*;
import cz.cvut.fel.klykadan.model.gameObject.CollisionBox;
import cz.cvut.fel.klykadan.view.GUICoinfig;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/**
 * Represents a friendly non-playable character (NPC) in the game.
 */
public class FriendlyNPC extends Character implements Animatable, Collidable, Interactable {
    private List<String> dialogues;
    private int dialogueIndex = 0;
    private boolean isMoving;
    private int initialXposition;
    private int initialYposition;
    private int maxHealth;
    private int originalSpeed;
    private boolean inInteraction = false;


    /**
     * Constructs a friendly NPC.
     * @param controller The game controller.
     * @param dialogues List of dialogues for the NPC.
     * @param cn GUI configuration.
     * @param textureM Texture manager.
     * @param xPosition Initial X position of the NPC.
     * @param yPosition Initial Y position of the NPC.
     * @param speed Speed of the NPC.
     * @param health Health of the NPC.
     */
    public FriendlyNPC(GameController controller, List<String> dialogues, GUICoinfig cn, TextureManager textureM, int xPosition, int yPosition, int speed, int health){
        super(controller, "NPC", null, cn, textureM, xPosition, yPosition, cn.getTileSize(),cn.getTileSize(), speed,health);
        this.dialogues = new ArrayList<>(dialogues);
        this.originalSpeed = speed;
        this.initialXposition = xPosition;
        this.initialYposition = yPosition;
        this.maxHealth = health;
        isMoving = true;
        loadImages();
        this.sprite = new Sprite(this, textureM, controller);
    }
    /**
     * Updates the NPC.
     */
    @Override
    public void update() {
        if (inInteraction) {
            updateDirectionTowardsPlayer(controller.getPlayer());
        }
        super.update();
    }
    /**
     * Performs the action of the NPC.
     */
    @Override
    public void doAction(){
        if (isMoving && FPClocker++ >= 60) {
            FPClocker = 0;
            Random random = new Random();
            int randInt = random.nextInt(4);
            direction = Direction.values()[randInt];
        }
    }
    /**
     * Interacts with the player.
     * @param player The player interacting with the NPC.
     */
    @Override
    public void interact(Player player) {
        inInteraction = true;
        payAttention(player);
        if (dialogueIndex < dialogues.size()) {
            controller.getUI().showDialogue(dialogues.get(dialogueIndex));
            dialogueIndex++;
        } else {
            resumeNormalBehavior();
        }
    }
    /**
     * Makes the NPC pay attention to the player during interaction.
     * @param player The player interacting with the NPC.
     */
    private void payAttention(Player player){
        speed = 0;
        isMoving = false;
    }
    /**
     * Resumes the normal behavior of the NPC after interaction.
     */
    private void resumeNormalBehavior() {
        controller.getUI().hideDialogue();
        speed = originalSpeed;
        isMoving = true;
        inInteraction = false;
        dialogueIndex = 0;
    }
    /**
     * Stops the interaction with the player.
     */
    public void stopInteraction(){
        if(inInteraction){
            resumeNormalBehavior();
        }
    }
    /**
     * Loads the images for the NPC.
     */
    private void loadImages() {
        try {
            upImages = new Image[] {
                    new Image("/npc/sprites/up1.png"),
                    new Image("/npc/sprites/up2.png")
            };
            downImages = new Image[] {
                    new Image("/npc/sprites/down1.png"),
                    new Image("/npc/sprites/down2.png")
            };
            leftImages = new Image[] {
                    new Image("/npc/sprites/left1.png"),
                    new Image("/npc/sprites/left2.png")
            };
            rightImages = new Image[] {
                    new Image("/npc/sprites/right1.png"),
                    new Image("/npc/sprites/right2.png")
            };

            upStand = new Image("/npc/sprites/upStand.png");
            downStand = new Image("/npc/sprites/downStand.png");
            leftStand = new Image("/npc/sprites/leftStand.png");
            rightStand = new Image("/npc/sprites/rightStand.png");
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
        return true;
    }
    public Sprite getSprite(){ return sprite; }

    public List<String> getDialogues() {
        return this.dialogues;
    }
}
