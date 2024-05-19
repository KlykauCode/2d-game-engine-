package cz.cvut.fel.klykadan.controller;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
/**
 * Handles keyboard events for the game, maintaining the current state of key presses.
 * This class tracks both simple key presses and complex behaviors like toggles or one-time triggers
 * for specific actions, facilitating game control through keyboard input.
 */

public class InputHandler implements EventHandler<KeyEvent> {

    private boolean upPressed, downPressed, leftPressed, rightPressed;
    private boolean shiftPressed;
    private boolean eIsPressed;
    private boolean fIsPressed;
    private boolean hIsPressed;
    private boolean rIsPressed;
    private boolean escIsPressed;
    private boolean spaceIsPressed;
    private boolean spaceWasPressed;
    private boolean fWasPressed;
    private boolean eWasPressed = false;
    private boolean escWasPressed = false;
    /**
     * Handles keyboard events for the game, maintaining the current state of key presses.
     * This class tracks both simple key presses and complex behaviors like toggles or one-time triggers
     * for specific actions, facilitating game control through keyboard input.
     */
    @Override
    public void handle(KeyEvent event) {
        boolean isPressed = event.getEventType() == KeyEvent.KEY_PRESSED;
        boolean isReleased = event.getEventType() == KeyEvent.KEY_RELEASED;
        KeyCode code = event.getCode();
        switch (code) {
            case W: upPressed = isPressed; break;
            case A: leftPressed = isPressed; break;
            case S: downPressed = isPressed; break;
            case D: rightPressed = isPressed; break;
            case H: hIsPressed = isPressed; break;
            case R: rIsPressed = isPressed; break;
            case SHIFT: shiftPressed = isPressed; break;
            case E:
                if (isPressed && !eWasPressed) {
                    eIsPressed = true;
                    eWasPressed = true;
                } else if (!isPressed) {
                    eIsPressed = false;
                    eWasPressed = false;
                }
                break;
            case ESCAPE:
                if (isPressed) {
                    if (!escWasPressed) {
                        escIsPressed = true;
                        escWasPressed = true;
                    }
                } else if (isReleased) {
                    escWasPressed = false;
                }
                break;
            case SPACE:
                spaceIsPressed = isPressed;
                break;
            case F:
                if (isPressed) {
                    if (!fWasPressed) {
                        fIsPressed = true;
                        fWasPressed = true;
                    }
                } else if (isReleased) {
                    fWasPressed = false;
                }
                break;
        }
        event.consume();
    }


    public boolean isMoving(){
        return upPressed || downPressed || leftPressed || rightPressed;
    }

    public boolean isUpPressed() {
        return upPressed;
    }

    public boolean isDownPressed() {
        return downPressed;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public boolean isShiftPressed() {
        return shiftPressed;
    }

    public boolean isEPressed() {
        return eIsPressed;
    }

    public boolean ishIsPressed() {
        return hIsPressed;
    }

    public boolean isrIsPressed() {
        return rIsPressed;
    }

    public void setEIsPressed(boolean eIsPressed) {
        this.eIsPressed = eIsPressed;
    }

    public void setSpaceIsPressed(boolean spaceIsPressed) {
        this.spaceIsPressed = spaceIsPressed;
    }

    public boolean isEscPressed() {
        if (escIsPressed) {
            escIsPressed = false;
            return true;
        }
        return false;
    }
    public boolean isSpacePressed(){
        return spaceIsPressed;
    }

    public boolean isfIsPressed() {
        if (fIsPressed) {
            fIsPressed = false;
            return true;
        }
        return false;
    }
}
