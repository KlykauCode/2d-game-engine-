package cz.cvut.fel.klykadan.controller;

import javafx.scene.image.Image;

public interface Animatable {
    Direction getDirection();
    int getX();
    int getY();
    int getScreenX();
    int getScreenY();
    boolean isMoving();
    Image[] getMovementImages();
    Image getStandingImage();
}
