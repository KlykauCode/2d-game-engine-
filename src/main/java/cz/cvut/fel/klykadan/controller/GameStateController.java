package cz.cvut.fel.klykadan.controller;

import java.util.logging.Logger;

/**
 * The GameStateController class manages the current state of the game, handling transitions
 * between various states like PLAY, PAUSE, GAME_OVER, START, and INVENTORY. It responds
 * to user inputs to change states and controls the game flow accordingly.
 */
public class GameStateController {
    public enum State {
        PLAY, PAUSE, GAME_OVER, START, INVENTORY
    }
    private static final Logger LOGGER = Logger.getLogger(GameStateController.class.getName());
    private State currentState;
    private InputHandler input;
    private GameController controller;
    /**
     * Constructs a GameStateController with a reference to the GameController and an InputHandler.
     * Initializes the game state to START and plays the menu theme.
     *
     * @param gameController the game controller that this state controller manipulates
     * @param input the input handler used to detect user actions
     */

    public GameStateController(GameController gameController, InputHandler input) {
        this.controller = gameController;
        this.input = input;
        this.currentState = State.START;
        controller.getAudioManager().playMenuTheme();
    }
    /**
     * Updates the game state based on user inputs. Checks for pause and inventory toggles.
     */
    public void update() {
        if (input.isEscPressed()) {
            togglePause();
        }

        if(input.isfIsPressed()){
            toggleInventory();
        }
    }
    /**
     * Toggles the inventory state. If the game is currently in PLAY mode, it switches to INVENTORY,
     * and vice versa.
     */
    private void toggleInventory(){
        if (currentState == State.PLAY) {
            setCurrentState(State.INVENTORY);
            LOGGER.info("Switched to inventory");
        } else if (currentState == State.INVENTORY) {
            setCurrentState(State.PLAY);
            LOGGER.info("Resumed game from inventory");
        }
    }
    /**
     * Toggles the game's pause state. If the game is currently in PLAY mode, it switches to PAUSE,
     * and if it's in PAUSE, it switches back to PLAY.
     */
    private void togglePause() {
        if (currentState == State.PLAY) {
            setCurrentState(State.PAUSE);
            LOGGER.info("Game paused");
        } else if (currentState == State.PAUSE) {
            setCurrentState(State.PLAY);
            LOGGER.info("Game resumed");
        }
    }

    public boolean isState(State state) {
        return currentState == state;
    }

    public State getCurrentState() {
        return currentState;
    }
    /**
     * Sets the current state of the game to a new state, handling necessary operations
     * such as starting or stopping sounds, and updating the UI accordingly.
     *
     * @param newState the new state to set
     */
    public void setCurrentState(State newState) {
        if (currentState != newState) {
            LOGGER.info("Changing state from " + currentState + " to " + newState);
            currentState = newState;
            controller.getAudioManager().stopAllSounds();
            switch (newState) {
                case START:
                    controller.getAudioManager().playMenuTheme();
                    break;
                case PAUSE:
                    controller.getAudioManager().playMenuTheme();
                    break;
                case PLAY:
                    controller.getAudioManager().playMainTheme();
                    break;
                case GAME_OVER:
                    break;
                case INVENTORY:
                    break;
            }
            controller.getUI().updateUI();
        }
    }
}

