package cz.cvut.fel.klykadan;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import cz.cvut.fel.klykadan.model.gameObject.characters.Inventory;
import cz.cvut.fel.klykadan.model.gameObject.items.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.UUID;

public class InventoryTest {
    private Inventory inventory;
    private Item mockedItem;

    @BeforeEach
    public void setUp() {
        inventory = new Inventory();
        mockedItem = mock(Item.class);
        when(mockedItem.getName()).thenReturn("apple");
        when(mockedItem.getId()).thenReturn(UUID.randomUUID());
    }

    @Test
    public void testAddItem() {
        inventory.addItem(mockedItem);
        assertTrue(inventory.hasItem("apple"));
        assertEquals(1, inventory.getItemCount("apple"));
    }

    @Test
    public void testRemoveItem() {
        inventory.addItem(mockedItem);
        inventory.removeItem(mockedItem);
        assertFalse(inventory.hasItem("apple"));
        assertEquals(0, inventory.getItemCount("apple"));
    }

    @Test
    public void testClearInventory() {
        inventory.addItem(mockedItem);
        inventory.clear();
        assertEquals(0, inventory.getItemCount("apple"));
    }

    @Test
    public void testGetItem() {
        inventory.addItem(mockedItem);
        Item retrievedItem = inventory.getItem("apple");
        assertNotNull(retrievedItem);
        assertEquals(mockedItem, retrievedItem);
    }

    @Test
    public void testGetAllItems() {
        inventory.addItem(mockedItem);
        inventory.addItem(mockedItem);
        assertEquals(2, inventory.getAllItems().size());
    }

    @Test
    public void testClearRemovedItems() {
        inventory.addItem(mockedItem);
        inventory.removeItem(mockedItem);
        inventory.clearRemovedItems();
        assertTrue(inventory.getRemovedItems().isEmpty());
    }
}


