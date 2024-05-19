package cz.cvut.fel.klykadan;

import static org.mockito.Mockito.*;

import cz.cvut.fel.klykadan.controller.Direction;
import cz.cvut.fel.klykadan.controller.GameController;
import cz.cvut.fel.klykadan.controller.PhysicsEngine;
import cz.cvut.fel.klykadan.controller.TextureManager;
import cz.cvut.fel.klykadan.model.gameObject.CollisionBox;
import cz.cvut.fel.klykadan.model.gameObject.GameObject;
import cz.cvut.fel.klykadan.model.gameObject.characters.Character;
import cz.cvut.fel.klykadan.model.gameObject.characters.Player;
import cz.cvut.fel.klykadan.view.GUICoinfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class PhysicsEngineTest {
    private PhysicsEngine physicsEngine;
    private GameController mockedController;
    private GUICoinfig mockedConfig;
    private TextureManager mockedTextureManager;
    private Character character;
    private GameObject gameObject;

    @BeforeEach
    void setUp() {
        mockedController = mock(GameController.class);
        mockedConfig = mock(GUICoinfig.class);
        mockedTextureManager = mock(TextureManager.class);
        character = mock(Character.class);
        gameObject = mock(GameObject.class);

        when(mockedController.getTextureM()).thenReturn(mockedTextureManager);
        when(mockedConfig.getTileSize()).thenReturn(48);
        when(mockedTextureManager.getMap()).thenReturn(new int[10][10]);
        when(mockedTextureManager.getCollisionTiles()).thenReturn(new boolean[100]);

        physicsEngine = new PhysicsEngine(mockedController, mockedConfig);
    }

    @Test
    void testCollisionWithGameObject() {
        when(character.getX()).thenReturn(100);
        when(character.getY()).thenReturn(100);
        when(character.getCollisionBox()).thenReturn(new CollisionBox(100, 100, 30, 30));
        when(character.getSpeed()).thenReturn(5);
        when(character.getDirection()).thenReturn(Direction.RIGHT);

        when(gameObject.getXposition()).thenReturn(130);
        when(gameObject.getYposition()).thenReturn(100);
        when(gameObject.getSizeX()).thenReturn(30);
        when(gameObject.getSizeY()).thenReturn(30);
        when(gameObject.isCollidable()).thenReturn(true);

        ArrayList<GameObject> gameObjects = new ArrayList<>();
        gameObjects.add(gameObject);
        when(mockedController.getGameObjects()).thenReturn(gameObjects);

        physicsEngine.detectCollision(character);

        verify(character).setCollision(true);
    }

    @Test
    void testNoCollision() {
        when(character.getX()).thenReturn(10);
        when(character.getY()).thenReturn(10);
        when(character.getCollisionBox()).thenReturn(new CollisionBox(10, 10, 20, 20));
        when(character.getSpeed()).thenReturn(5);
        when(character.getDirection()).thenReturn(Direction.UP);

        when(mockedController.getGameObjects()).thenReturn(new ArrayList<>());

        physicsEngine.detectCollision(character);

        verify(character, never()).setCollision(true);
    }
}