package cz.cvut.fel.klykadan.controller;

import cz.cvut.fel.klykadan.model.gameObject.GameObject;
import cz.cvut.fel.klykadan.model.gameObject.characters.Character;
import cz.cvut.fel.klykadan.model.gameObject.characters.Enemy;
import cz.cvut.fel.klykadan.model.gameObject.characters.FriendlyNPC;
import cz.cvut.fel.klykadan.model.gameObject.characters.Player;
import cz.cvut.fel.klykadan.view.GUICoinfig;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
/**
 * The PhysicsEngine class manages the physical interactions within the game,
 * including collision detection between characters and game objects or environment obstacles.
 * It uses the game configuration and the GameController to access game data necessary for physics calculations.
 */

public class PhysicsEngine {
    private GameController controller;
    private GUICoinfig cn;
    private Player player;
    /**
     * Constructs a PhysicsEngine with a reference to the GameController and the game's GUI configuration.
     *
     * @param controller the main game controller that provides access to the game state and objects
     * @param cn the GUI configuration settings, often including tile sizes and layout configurations
     */
    public PhysicsEngine(GameController controller, GUICoinfig cn){
        this.controller = controller;
        this.cn = cn;
    }
    /**
     * Detects potential collisions for a given character based on its current trajectory and speed.
     * This method checks for collisions against the environment (like map tiles) and other collidable game objects.
     * If a collision is detected, it updates the character's collision status.
     *
     * @param character the character whose collisions are to be checked, including players and NPCs
     */
    public void detectCollision(Character character) {

        int characterXPosition = character.getX();
        int characterYPosition = character.getY();
        int boxWidth = character.getCollisionBox().getSizeX();
        int boxHeight = character.getCollisionBox().getSizeY();
        int characterSpeed = character.getSpeed();

        int futureX = characterXPosition + 15; //box x position for updates
        int futureY = characterYPosition + 25; //box y position for updates

        switch (character.getDirection()) {
            case UP:
                futureY -= characterSpeed;
                break;
            case DOWN:
                futureY += characterSpeed;
                break;
            case LEFT:
                futureX -= characterSpeed;
                break;
            case RIGHT:
                futureX += characterSpeed;
                break;
        }
        boolean collisionDetected = false;

        int[][] tilesToCheck = {
                {futureX , futureY}, //top-left
                {futureX + boxWidth, futureY}, //top-right
                {futureX, futureY + boxHeight}, //bottom-left
                {futureX + boxWidth, futureY + boxHeight} //bottom-right
        };
        for (int[] point : tilesToCheck) {
            int col = point[0] / cn.getTileSize();
            int row = point[1] / cn.getTileSize();
            if (row >= 0 && row < controller.getTextureM().getMap()[0].length && col >= 0 && col < controller.getTextureM().getMap().length) {
                int tileNum = controller.getTextureM().getMap()[col][row];
                if (controller.getTextureM().getCollisionTiles()[tileNum]) {
                    character.setCollision(true);
                    return;
                }
            }
        }
        for(GameObject obj : controller.getGameObjects()){
            if(obj.isCollidable() && character != obj){
                if (futureX < obj.getXposition() + obj.getSizeX() &&
                        futureX + boxWidth > obj.getXposition() &&
                        futureY < obj.getYposition() + obj.getSizeY() &&
                        futureY + boxHeight > obj.getYposition()) {
                    collisionDetected = true;
                    break;
                }
            }
        }
        character.setCollision(collisionDetected);
    }
}
