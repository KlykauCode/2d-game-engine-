package cz.cvut.fel.klykadan.controller;
import cz.cvut.fel.klykadan.model.gameObject.Bullet;
import cz.cvut.fel.klykadan.model.gameObject.CollisionBox;
import cz.cvut.fel.klykadan.model.gameObject.GameObject;
import cz.cvut.fel.klykadan.model.gameObject.characters.Player;
import cz.cvut.fel.klykadan.model.gameObject.items.Key;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import cz.cvut.fel.klykadan.view.GUICoinfig;
import cz.cvut.fel.klykadan.view.GamePanel;
import javafx.scene.paint.Color;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.Scanner;

/**
 * The TextureManager class handles all operations related to loading, managing,
 * and drawing textures in the game. It includes functionality for initializing the game map,
 * loading texture images, and rendering textures based on the player's position within the game world.
 */
public class TextureManager {
    private GraphicsContext gc;
    private GUICoinfig cn;
    private Image[] texture;
    private Player player;
    private GameController controller;
    private int map[][];
    private boolean collisionTiles[];

    /**
     * Constructs a TextureManager with specified graphics context, GUI configuration, and game controller.
     *
     * @param gc the graphics context used for drawing textures
     * @param cn GUI configuration settings that include tile sizes and level dimensions
     * @param controller the main game controller that coordinates game state and logic
     */

    public TextureManager(GraphicsContext gc, GUICoinfig cn, GameController controller){
        this.gc = gc;
        this.cn = cn;
        this.controller = controller;
        this.texture = new Image[50];
        this.collisionTiles = new boolean[texture.length];
        map = new int[cn.getLevelSizeX()][cn.getLevelSizeY()];
        initCollisionTiles();
        getTexture();
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
    /**
     * Initializes the game map from a specified file path, loading the layout into the map array.
     *
     * @param mapPath the path to the map file, which specifies the layout of tiles
     */

    public void initMap(String mapPath){
        try{
            InputStream is = getClass().getResourceAsStream(mapPath);
            if (is == null) {
                throw new FileNotFoundException("Resource " + mapPath + " not found.");
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));


            int fieldWidth = cn.getLevelSizeX();
            int fieldHeight = cn.getLevelSizeY();

            String line;
            for (int row = 0; row < fieldHeight; row++) {
                line = br.readLine();
                if (line == null) {
                    break;
                }
                String[] tiles = line.trim().split("\\s+");
                for (int col = 0; col < fieldWidth && col < tiles.length; col++) {
                    map[col][row] = Integer.parseInt(tiles[col]);
                }
            }
            br.close();
        } catch(Exception e){
            e.printStackTrace();
        }

    }


    private void getTexture(){
        try{
            texture[0] = new Image("/textures/lab/tiles.png");
            texture[1] = new Image("/textures/lab/tile6.png");
            texture[2] = new Image("/textures/doors/OFFICEDOOR.png");
            texture[3] = new Image("/textures/lab/sleft2.png");
            texture[4] = new Image("/textures/lab/tile110.png");
            texture[5] = new Image("/textures/lab/sup.png");
            texture[6] = new Image("/textures/lab/tile7.png");
            texture[7] = new Image("/textures/lab/tile8.8.png");
            texture[8] = new Image("/textures/lab/specialTile.png");
            texture[9] = new Image("/textures/doors/door_green.png");

        } catch (Exception e){
            System.err.println("Texture not found: " + e.getMessage());
        }
    }
    private void initCollisionTiles() {
        collisionTiles[1] = true;
        collisionTiles[4] = true;
        collisionTiles[6] = true;
    }
    /**
     * Draws the textures for the entire game level based on the player's current position,
     * ensuring that only visible portions of the map are rendered to optimize performance.
     *
     * @param gc the graphics context on which textures are drawn
     */
    public void drawTexture(GraphicsContext gc) {
        int tileSize = cn.getTileSize();
        int levelSizeX = cn.getLevelSizeX();
        int levelSizeY = cn.getLevelSizeY();

        double playerPosX = player.getX();
        double playerPosY = player.getY();
        double playerSizeX = player.getScreenX();
        double playerSizeY = player.getScreenY();

        double playerMinX = playerPosX - playerSizeX;
        double playerMaxX = playerPosX + playerSizeX;
        double playerMinY = playerPosY - playerSizeY;
        double playerMaxY = playerPosY + playerSizeY;

        for (int row = 0; row < levelSizeY; row++) {
            int y = row * tileSize;
            double screenY = y - playerPosY + playerSizeY;

            for (int col = 0; col < levelSizeX; col++) {
                int x = col * tileSize;
                double screenX = x - playerPosX + playerSizeX;
                int tileNum = map[col][row];

                if (x + tileSize > playerMinX &&
                        x - tileSize < playerMaxX &&
                        y + tileSize > playerMinY &&
                        y - tileSize < playerMaxY) {
                    gc.drawImage(texture[tileNum], screenX, screenY, tileSize, tileSize);
                }
            }
        }
    }
    /**
     * Draws all visible game objects using the graphics context. Each object's position is adjusted
     * based on the player's position to ensure correct placement on the screen.
     *
     * @param gc the graphics context on which game objects are drawn
     */

    public void drawGameObjects(GraphicsContext gc) {
        for (GameObject obj : controller.getGameObjects()) {
            if (obj.isVisible()) {

                double objX = obj.getXposition() - player.getXposition() + player.getScreenX();
                double objY = obj.getYposition() - player.getYposition() + player.getScreenY();

                gc.drawImage(obj.getImage(), objX, objY, cn.getTileSize(), cn.getTileSize());
            }
            obj.update();
        }
    }
    public int[][] getMap() {
        return map;
    }

    public boolean[] getCollisionTiles() {
        return collisionTiles;
    }
}

