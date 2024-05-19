package cz.cvut.fel.klykadan.controller;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.net.URL;

/**
 * The AudioManager class handles all audio operations within the game. It manages
 * playing, stopping, and controlling volume for various sound effects and background music.
 * This class utilizes the JavaFX MediaPlayer to handle media playback.
 */
public class AudioManager {
    private MediaPlayer backgroundPlayer;
    private MediaPlayer movePlayer;
    private int stepSoundCounter = 0;
    /**
     * Plays a sound from a specified file with options to loop and set volume.
     * If the sound file is a theme, it replaces any currently playing background music.
     *
     * @param soundFile The path to the sound file.
     * @param loop Whether the sound should loop continuously.
     * @param volume The volume at which to play the sound, where 1.0 is the loudest.
     */
    public void playSound(String soundFile, boolean loop, double volume) {
        URL resource = getClass().getResource(soundFile);
        if (resource == null) {
            System.err.println("Cannot find file: " + soundFile);
            return;
        }
        Media media = new Media(resource.toString());
        MediaPlayer player = new MediaPlayer(media);
        if (loop) {
            player.setCycleCount(MediaPlayer.INDEFINITE);
        }
        player.setVolume(volume);

        if (soundFile.contains("maintheme") || soundFile.contains("menu")) {
            if (backgroundPlayer != null) {
                backgroundPlayer.stop();
                backgroundPlayer.dispose();
            }
            backgroundPlayer = player;
        }
        player.play();
    }


    /**
     * Plays the main theme music of the game, setting it to loop indefinitely at a moderate volume.
     */
    public void playMainTheme(){
        playSound("/sounds/maintheme.wav", true,0.7);
    }
    /**
     * Plays footstep sounds with alternating sound files to simulate walking,
     * triggered every two calls to this method to vary the sound.
     */
    public void playMoveSound() {
        if (stepSoundCounter % 2 == 0) {
            playSound("/sounds/step1.wav", false, 0.5);
        } else {
            playSound("/sounds/step2.wav", false, 0.5);
        }
        stepSoundCounter++;
    }
    /**
     * Plays the sound of a gunshot. Intended to be used when a weapon is fired.
     */
    public void playShootSound(){
        playSound("/sounds/shot.wav", false,0.2);
    }
    /**
     * Plays the sound of reloading a weapon. This is used to enhance the realism
     * when the player reloads their weapon.
     */
    public void playReloadSound(){
        playSound("/sounds/reload.wav", false,0.2);
    }

    /**
     * Plays a sound indicating the weapon is empty. Typically used when the player
     * attempts to fire a weapon without ammunition.
     */
    public void playEmptySound(){
        playSound("/sounds/empty.wav", false,0.2);
    }
    /**
     * Plays a generic action sound, such as picking up an item or interacting with an object.
     */
    public void playActionSound(){
        playSound("/sounds/take.wav", false,0.4);
    }
    /**
     * Plays the sound of a door opening. Used when the player successfully opens a door.
     */
    public void playOpenDoorSound(){
        playSound("/sounds/door.wav", false,0.8);
    }
    /**
     * Plays the sound of a door being unable to open, typically used when the door is locked.
     */
    public void playClosedDoorSound(){
        playSound("/sounds/closed.wav", false,0.8);
    }

    /**
     * Plays the menu theme music of the game, looping it indefinitely at a higher volume for menu ambiance.
     */
    public void playMenuTheme(){
        playSound("/sounds/menu.wav", true, 0.8);
    }
    /**
     * Stops all currently playing sounds and releases any system resources associated with them.
     * This includes any background or move sounds that are currently active.
     */
    public void stopAllSounds() {
        if (backgroundPlayer != null) {
            backgroundPlayer.stop();
            backgroundPlayer.dispose();
            backgroundPlayer = null;
        }
        if (movePlayer != null) {
            movePlayer.stop();
            movePlayer.dispose();
            movePlayer = null;
        }
    }
}
