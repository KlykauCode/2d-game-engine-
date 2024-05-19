package cz.cvut.fel.klykadan.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.fel.klykadan.model.gameObject.Door;
import cz.cvut.fel.klykadan.model.gameObject.GameObject;
import cz.cvut.fel.klykadan.model.gameObject.LevelDoor;
import cz.cvut.fel.klykadan.model.gameObject.characters.Enemy;
import cz.cvut.fel.klykadan.model.gameObject.characters.FriendlyNPC;
import cz.cvut.fel.klykadan.model.gameObject.items.*;
import cz.cvut.fel.klykadan.view.GUICoinfig;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
/**
 * The LevelController class is responsible for managing the loading and setup of different levels in the game.
 * It controls the initialization of game objects, NPCs, enemies, and the map configuration based on level-specific
 * data files.
 */
public class LevelController {
    private GameController controller;
    private GUICoinfig cn;


    /**
     * Constructs a LevelController with a reference to the GameController and the game's GUI configuration.
     *
     * @param controller the main game controller that this level controller will interact with
     * @param cn the GUI configuration settings used throughout the game
     */
    public LevelController(GameController controller, GUICoinfig cn) {
        this.controller = controller;
        this.cn = cn;
    }

    /**
     * Loads the specified level by reading configuration files for objects, entities, and the map.
     * It sets up the game environment according to the level details provided in JSON format.
     *
     * @param levelNumber the level number to load
     */
    public void loadLevel(int levelNumber) {
        try {
            String objectsConfigPath = "";
            String entityConfigPath = "";
            String mapPath = "";

            if (levelNumber == 1) {
                objectsConfigPath = "levels/level1/objectsConfig1.json";
                entityConfigPath = "levels/level1/entityConfig1.json";
                mapPath = "/maps/testmap2.txt";
            } else if (levelNumber == 2) {
                objectsConfigPath = "levels/level2/objectsConfig2.json";
                entityConfigPath = "levels/level2/entityConfig2.json";
                mapPath = "/maps/testmap1.txt";
            } else {
                System.out.println("Level not found");
                return;
            }

            setupGameObjects(objectsConfigPath);
            setCharacter(entityConfigPath);
            controller.getTextureM().initMap(mapPath);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading level: " + levelNumber);
        }
    }
    /**
     * Sets up the game objects for the current level based on a configuration path.
     * This includes instantiating and positioning all static and interactive objects within the game.
     *
     * @param objectsLevelPath the file path to the JSON configuration for game objects
     */
    public void setupGameObjects(String objectsLevelPath){
        try {
            int tileSize = cn.getTileSize();
            ObjectMapper objectMapper = new ObjectMapper();
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(objectsLevelPath);
            JsonNode rootNode = objectMapper.readTree(inputStream);

            JsonNode gameObjectsArray = rootNode.get("gameObjects");
            for (JsonNode gameObjectNode : gameObjectsArray) {
                String type = gameObjectNode.get("type").asText();
                JsonNode position = gameObjectNode.get("position");
                int x = position.get("x").asInt() * tileSize;
                int y = position.get("y").asInt() * tileSize;
                boolean isLocked = gameObjectNode.has("isLocked") && gameObjectNode.get("isLocked").asBoolean();

                switch (type) {
                    case "Key":
                        controller.getGameObjects().add(new Key(x, y, tileSize, tileSize));
                        break;
                    case "Door":
                        controller.getGameObjects().add(new Door(x, y, tileSize, tileSize, isLocked));
                        break;
                    case "Weapon":
                        controller.getGameObjects().add(new Weapon(cn, x, y, tileSize, tileSize));
                        break;
                    case "Ammo":
                        controller.getGameObjects().add(new Ammo(x, y, tileSize, tileSize));
                        break;
                    case "Heal":
                        controller.getGameObjects().add(new Heal(x, y, tileSize, tileSize));
                        break;
                    case "Detail":
                        controller.getGameObjects().add(new Detail(x, y, tileSize, tileSize));
                        break;
                    case "LevelDoor":
                        controller.getGameObjects().add(new LevelDoor(x, y, tileSize, tileSize, isLocked));
                        break;
                    case "LevelKey":
                        controller.getGameObjects().add(new LevelKey(x, y, tileSize, tileSize));
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Configures characters, including NPCs and enemies, for the current level based on a configuration path.
     * This method parses a JSON file to create and position all characters with their specified attributes.
     *
     * @param charactersLevelPath the file path to the JSON configuration for characters
     */

    public void setCharacter(String charactersLevelPath) {
        try {
            int tileSize = cn.getTileSize();
            ObjectMapper objectMapper = new ObjectMapper();
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(charactersLevelPath);
            JsonNode rootNode = objectMapper.readTree(inputStream);

            JsonNode npcsArray = rootNode.get("npcs");

            for (JsonNode npcNode : npcsArray) {
                List<String> dialogues = new ArrayList<>();
                npcNode.get("dialogues").forEach(dialogue -> dialogues.add(dialogue.asText()));
                JsonNode position = npcNode.get("position");
                int x = position.get("x").asInt() * tileSize;
                int y = position.get("y").asInt() * tileSize;
                int speed = npcNode.get("speed").asInt();
                int health = npcNode.get("health").asInt();

                FriendlyNPC npc = new FriendlyNPC(controller, dialogues, cn, controller.getTextureM(), x, y, speed, health);
                controller.getNpcs().add(npc);
            }
            JsonNode enemiesArray = rootNode.get("enemies");

            for (JsonNode enemyNode : enemiesArray) {
                JsonNode position = enemyNode.get("position");
                int x = position.get("x").asInt() * tileSize;
                int y = position.get("y").asInt() * tileSize;
                int speed = enemyNode.get("speed").asInt();
                int health = enemyNode.get("health").asInt();

                Enemy enemy = new Enemy(controller, cn, controller.getTextureM(), x, y, speed, health);
                controller.getEnemies().add(enemy);
            }
        } catch (Exception e) {
            System.out.println("Cannot read npc from json");
        }
    }
}
