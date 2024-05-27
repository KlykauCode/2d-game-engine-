package cz.cvut.fel.klykadan.model.gameObject.characters;

import cz.cvut.fel.klykadan.model.gameObject.items.Item;

import java.util.*;
import java.util.logging.Logger;
import java.util.logging.Level;
/**
 * Represents the inventory of the player.
 */
public class Inventory {
    private Map<String, List<Item>> items; //klic a hodnoty se stejnym jmenem
    private Set<UUID> removedItems;
    private static final Logger LOGGER = Logger.getLogger(Inventory.class.getName());

    public Inventory() {
        items = new HashMap<>();
        removedItems = new HashSet<>();
    }
    /**
     * Adds an item to the inventory.
     * @param item The item to add.
     */
    public void addItem(Item item) {
        removedItems.remove(item.getId());
        if (!items.containsKey(item.getName())) {
            items.put(item.getName(), new ArrayList<>()); //pridavam list pro items se stejnym jmenem
        }
        items.get(item.getName()).add(item);

        LOGGER.log(Level.INFO, "Item added: {0}. Count: {1}", new Object[]{item.getName(), getItemCount(item.getName())});
    }

    /**
     * Clears the inventory.
     */
    public void clear() {
        items.clear();
        LOGGER.info("Inventory has been cleared");
    }
    /**
     * Clears the set of removed items.
     */
    public void clearRemovedItems(){
        removedItems.clear();
        LOGGER.info("Cleared removed items");
    }
    /**
     * Checks if the inventory contains a specific item.
     * @param itemName The name of the item to check.
     * @return True if the inventory contains the item, false otherwise.
     */
    public boolean hasItem(String itemName) {
        return items.containsKey(itemName) && !items.get(itemName).isEmpty();
    }
    /**
     * Removes an item from the inventory.
     * @param item The item to remove.
     */
    public void removeItem(Item item) {
        List<Item> itemList = items.get(item.getName());
        if (itemList != null && !itemList.isEmpty()) {
            itemList.remove(item);
            if (itemList.isEmpty()) {
                removedItems.add(item.getId());
            }
            LOGGER.log(Level.INFO, "Item removed: {0}. Count: {1}", new Object[]{item.getName(), getItemCount(item.getName())});
        } else {
            LOGGER.log(Level.WARNING, "No {0} to remove.", item.getName());
        }
    }
    /**
     * Uses an item from the inventory.
     * @param itemName The name of the item to use.
     */
    public void useItem(String itemName) {
        if (hasItem(itemName)) {
            Item item = items.get(itemName).get(0);
            removeItem(item);
            LOGGER.info("Used " + itemName);
        } else {
            LOGGER.log(Level.WARNING, "No {0} to use.", itemName);
        }
    }
    /**
     * Gets the count of a specific item in the inventory.
     * @param itemName The name of the item.
     * @return The count of the item in the inventory.
     */
    public int getItemCount(String itemName) {
        return items.getOrDefault(itemName, new ArrayList<>()).size();
    }
    /**
     * Gets all items in the inventory.
     * @return A map of item names and their corresponding list of items.
     */
    public Map<String, List<Item>> getItems() {
        return items;
    }

    /**
     * Gets a specific item from the inventory.
     * @param itemName The name of the item to get.
     * @return The item if found, otherwise null.
     */
    public Item getItem(String itemName) {
        if (hasItem(itemName)) {
            return items.get(itemName).get(0);
        } else {
            LOGGER.log(Level.WARNING, "No item named {0} found in inventory.", itemName);
            return null;
        }
    }

    /**
     * Gets all items in the inventory.
     * @return A list of all items in the inventory.
     */
    public List<Item> getAllItems() {
        List<Item> allItems = new LinkedList<>();
        items.values().forEach(allItems::addAll);
        return allItems;
    }
    /**
     * Gets the set of removed item IDs.
     * @return The set of removed item IDs.
     */
    public Set<UUID> getRemovedItems() {
        return new HashSet<>(removedItems);
    }
}
