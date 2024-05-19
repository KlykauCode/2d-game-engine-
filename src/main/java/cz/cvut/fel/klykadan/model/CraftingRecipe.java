package cz.cvut.fel.klykadan.model;

import cz.cvut.fel.klykadan.controller.GameController;
import cz.cvut.fel.klykadan.model.gameObject.items.Detail;
import cz.cvut.fel.klykadan.model.gameObject.items.Item;
import cz.cvut.fel.klykadan.model.gameObject.items.SuperWeapon;
import cz.cvut.fel.klykadan.model.gameObject.items.Weapon;
import cz.cvut.fel.klykadan.view.GUICoinfig;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.logging.Level;
/**
 * The CraftingRecipe class manages crafting recipes for the game, allowing items to be combined into new items.
 * It supports adding new recipes and crafting items based on the available recipes.
 */
public class CraftingRecipe {
    private List<Recipe> recipes;
    private static final Logger LOGGER = Logger.getLogger(CraftingRecipe.class.getName());
    /**
     * Constructs a CraftingRecipe object, initializing the list of recipes and updating it based on the game state.
     *
     * @param controller the game controller that provides context and access to the game's objects
     * @param cn the game's GUI configuration, which may influence how recipes are processed or displayed
     */
    public CraftingRecipe(GameController controller, GUICoinfig cn) {
        recipes = new ArrayList<>();
        updateRecipe(controller, cn);
    }

    /**
     * Updates the list of available recipes based on the current game state. Typically called when game state changes
     * that could affect available items or their eligibility for crafting.
     *
     * @param controller the game controller used to access current game objects
     * @param cn the GUI configuration settings used within the game
     */
    public void updateRecipe(GameController controller, GUICoinfig cn){
        Item weapon = controller.findGameObject(Weapon.class);
        Item detail = controller.findGameObject(Detail.class);
        if (weapon == null || detail == null) {
            return;
        }
        Item superWeapon = new SuperWeapon(cn);
        addRecipe(weapon, detail, superWeapon);
        LOGGER.log(Level.INFO, "Recipe added with: {0} and {1}", new Object[]{weapon.getName(), detail.getName()});
    }
    /**
     * Adds a new crafting recipe to the list of available recipes.
     *
     * @param item1 the first item required for the recipe
     * @param item2 the second item required for the recipe
     * @param result the item that is produced by the recipe
     */
    public void addRecipe(Item item1, Item item2, Item result) {
        recipes.add(new Recipe(item1, item2, result));
    }

    /**
     * Attempts to craft an item by combining two provided items according to the available recipes.
     * If a matching recipe is found, returns the result of the recipe.
     *
     * @param item1 the first input item for crafting
     * @param item2 the second input item for crafting
     * @return the crafted item if a matching recipe is found, or null if no recipe matches
     */
    public Item craft(Item item1, Item item2) {
        for (Recipe recipe : recipes) {
            if (recipe.matches(item1, item2)) {
                return recipe.getResult();
            }
        }
        return null;
    }
}