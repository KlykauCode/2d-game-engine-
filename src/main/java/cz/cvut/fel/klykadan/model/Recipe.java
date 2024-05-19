package cz.cvut.fel.klykadan.model;

import cz.cvut.fel.klykadan.model.gameObject.items.Item;
import java.util.logging.Logger;
import java.util.logging.Level;
/**
 * The Recipe class represents a crafting recipe in the game, which defines how two items can be combined
 * to produce a new item. It stores both ingredients and the result of combining these ingredients.
 */
public class Recipe {
    private Item ingredient1;
    private Item ingredient2;
    private Item result;
    private static final Logger LOGGER = Logger.getLogger(Recipe.class.getName());
    /**
     * Constructs a Recipe with specified ingredients and a result.
     *
     * @param ingredient1 the first ingredient of the recipe
     * @param ingredient2 the second ingredient of the recipe
     * @param result the item produced by combining the two ingredients
     */

    public Recipe(Item ingredient1, Item ingredient2, Item result) {
        this.ingredient1 = ingredient1;
        this.ingredient2 = ingredient2;
        this.result = result;
    }
    /**
     * Determines if two provided items match the ingredients of this recipe, regardless of their order.
     * This method is useful for checking if a particular combination of items can be used to craft
     * the result of this recipe.
     *
     * @param item1 the first item to check against the recipe ingredients
     * @param item2 the second item to check against the recipe ingredients
     * @return true if the items match the recipe ingredients, false otherwise
     */
    public boolean matches(Item item1, Item item2) {
        if (ingredient1 == null || ingredient2 == null || item1 == null || item2 == null) {
            LOGGER.warning("One of the ingredients is null");
            return false;
        }
        return (ingredient1.getName().equals(item1.getName()) && ingredient2.getName().equals(item2.getName())) ||
                (ingredient1.getName().equals(item2.getName()) && ingredient2.getName().equals(item1.getName()));
    }

    public Item getResult() {
        return result;
    }
}
