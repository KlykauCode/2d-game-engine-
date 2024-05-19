package cz.cvut.fel.klykadan.controller;

import cz.cvut.fel.klykadan.model.gameObject.characters.Player;

public interface Interactable {
    void interact(Player player);
    boolean isInteractable();
    boolean isCollectible();
}
