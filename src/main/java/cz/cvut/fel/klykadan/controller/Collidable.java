package cz.cvut.fel.klykadan.controller;

import cz.cvut.fel.klykadan.model.gameObject.CollisionBox;

public interface Collidable {
    CollisionBox getCollisionBox();
    boolean isCollidable();
    void setCollision(boolean hasCollided);
}
