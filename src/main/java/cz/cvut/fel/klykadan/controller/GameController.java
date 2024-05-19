package cz.cvut.fel.klykadan.controller;

import cz.cvut.fel.klykadan.model.CraftingRecipe;
import cz.cvut.fel.klykadan.model.gameObject.GameObject;
import cz.cvut.fel.klykadan.model.gameObject.characters.Enemy;
import cz.cvut.fel.klykadan.model.gameObject.characters.FriendlyNPC;
import cz.cvut.fel.klykadan.model.gameObject.characters.Player;
import cz.cvut.fel.klykadan.model.gameObject.items.Item;
import cz.cvut.fel.klykadan.model.gameObject.items.SuperWeapon;
import cz.cvut.fel.klykadan.model.gameObject.items.Weapon;
import cz.cvut.fel.klykadan.view.GUICoinfig;
import cz.cvut.fel.klykadan.view.UI;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
/**
 * The GameController class manages core aspects of the gameplay, including game initialization,
 * game loop control, and level state management. It coordinates actions between various game components
 * such as input handling, audio management, physics, and texture management.
 *
 * @param cn GUI configuration settings
 * @param input handler for user inputs
 * @param gc graphics context for rendering
 * @param UI user interface manager
 */
public class GameController {
    private AudioManager audioManager;
    private InputHandler input;
    private GUICoinfig cn;
    private GraphicsContext gc;
    private TextureManager textureM;
    private PhysicsEngine physics;
    private AnimationTimer gameLoop;
    private CraftingRecipe craftingRecipe;
    private Player player;
    private LevelController levelController;
    private List<GameObject> gameObjects;
    private List<FriendlyNPC> npcs;
    private List<Enemy> enemies;
    private UI UI;
    private GameStateController stateController;
    private static final Logger LOGGER = Logger.getLogger(GameController.class.getName());
    private int currentLevel;
    public GameController(GUICoinfig cn, InputHandler input, GraphicsContext gc, UI UI) {
        this.cn = cn;
        this.gc = gc;
        this.input = input;
        this.UI = UI;
        audioManager = new AudioManager();
        this.stateController = new GameStateController(this, input);
        this.gameObjects = new ArrayList<>();
        textureM = new TextureManager(gc, cn, this);
        player = new Player(this, input, cn, textureM, audioManager);
        textureM.setPlayer(player);
        npcs = new ArrayList<>();
        enemies = new ArrayList<>();
        this.levelController = new LevelController(this, cn);
        physics = new PhysicsEngine(this, cn);
        craftingRecipe = new CraftingRecipe(this, cn);

        setGameObjects();
        initialize();
    }
    public void setUI(UI UI) {
        this.UI = UI;
    }

    public void setGameObjects(){
        levelController.loadLevel(currentLevel);

    }
    public void switchToNextLevel(int currentLevel) {
        gameObjects.clear();
        npcs.clear();
        enemies.clear();
        int nextLevel = currentLevel + 1;
        player.setCurrentLevel(nextLevel);
        levelController.loadLevel(nextLevel);
        LOGGER.log(Level.INFO, "Switching to level {0}", nextLevel);
        craftingRecipe.updateRecipe(this,cn);
    }
    /**
     * Initializes the game loop and starts its execution.
     */
    public void initialize(){
        gameLoop = new AnimationTimer() {
            long lastUpdate = 0;
            @Override
            public void handle(long now) {
                if (now - lastUpdate >= 1000000000 / cn.getFPS()) {
                    update();
                    render();
                    lastUpdate = now;
                }
            }
        };
        gameLoop.start();
    }
    /**
     * Starts the game loop if it is not already running.
     */
    public void startGameLoop(){
        if (gameLoop != null) {
            gameLoop.start();
        }
    }
    /**
     * Stops the game loop and all sound effects.
     */
    public void stopGameLoop(){
        if (gameLoop != null) {
            gameLoop.stop();
            audioManager.stopAllSounds();
        }
        for (Enemy enemy : enemies) {
            enemy.stopThread();
        }
    }
    /**
     * Removes a specific game object from the current list of active game objects.
     *
     * @param gameObject the game object to remove
     */
    public void removeGameObject(GameObject gameObject) {
        gameObjects.remove(gameObject);
    }
    /**
     * Updates the game state, including updating all game objects, NPCs, and enemies,
     * as well as checking game state conditions.
     */
    public void update(){
        stateController.update();
        currentLevel = player.getCurrentLevel();
        UI.updateUI();
        if(stateController.isState(GameStateController.State.PLAY)){
            player.updatePlayer();
            for (FriendlyNPC npc : npcs) {
                npc.update();
            }
            for (Enemy enemy: enemies){
                enemy.update();
            }
            gameObjects.removeIf(obj -> !obj.isVisible());
            enemies.removeIf(enemy -> !enemy.isVisible());
        }
    }
    /**
     * Renders the current game state to the screen, drawing all visual elements such as game objects,
     * player sprites, NPCs, and UI components based on the current game state.
     */
    public void render(){
        stateController.update();
        if(stateController.isState(GameStateController.State.PLAY)){
            gc.setFill(Color.BLACK);
            gc.fillRect(0, 0, cn.getScreenWidth(), cn.getScreenHeight());
            textureM.drawTexture(gc);
            textureM.drawGameObjects(gc);
            player.getSprite().drawPlayer(gc, cn);
            for (FriendlyNPC npc : npcs) {
                npc.getSprite().drawNPC(gc, cn);
            }
            for (Enemy enemy: enemies){
                enemy.getSprite().drawNPC(gc, cn);
            }
        }
    }

    /**
     * Resets the game to its initial state, stopping the game loop, clearing all objects, and reinitializing the game.
     */
    public void resetGame() {
        stopGameLoop();
        audioManager.stopAllSounds();

        gameObjects.clear();
        npcs.clear();
        player.reset();
        clearEnemies();

        levelController.loadLevel(player.getCurrentLevel());
        craftingRecipe.updateRecipe(this, cn);
        startGameLoop();
    }
    public void clearEnemies(){
        for (Enemy enemy : enemies) {
            enemy.stopThread();
        }
        enemies.clear();
    }
    public void saveGame(){
        GameSaverLoader.getInstance().saveGame(this,cn);
    }
    public void loadGame(){
        GameSaverLoader.getInstance().loadGame(this, cn);
    }

    public PhysicsEngine getPhysics() {
        return physics;
    }

    public TextureManager getTextureM() {
        return textureM;
    }

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    public Player getPlayer() {
        return player;
    }

    public List<FriendlyNPC> getNpcs() {
        return npcs;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public UI getUI() {
        return UI;
    }

    public GameStateController getStateController() {
        return stateController;
    }
    public AudioManager getAudioManager() {
        return audioManager;
    }

    public CraftingRecipe getCraftingRecipe() {
        return craftingRecipe;
    }

    public <Type extends GameObject> Type findGameObject(Class<Type> type) {
        for (GameObject obj : gameObjects) {
            if (type.isInstance(obj)) {
                return type.cast(obj);
            }
        }
        return null;
    }
}
