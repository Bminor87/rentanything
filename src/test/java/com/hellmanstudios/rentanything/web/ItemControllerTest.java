package com.hellmanstudios.rentanything.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.hellmanstudios.rentanything.entities.Item;
import com.hellmanstudios.rentanything.repository.ItemRepository;

import com.hellmanstudios.rentanything.web.ItemController;

import org.junit.jupiter.api.Test;

public class ItemControllerTest {

    private final ItemRepository itemRepository;
    private final ItemController itemController;

    public ItemControllerTest(ItemRepository itemRepository, ItemController itemController) {
        this.itemRepository = itemRepository;
        this.itemController = itemController;
    }
    
    @Test
    public void testGetItems() {
        ItemController itemController = new ItemController();
        List<Item> items = new ArrayList<>();
        items.add(new Item());
        items.add(new Item());
        items.add(new Item());
        itemController.setItems(items);
        assertEquals(3, itemController.getItems().size());
    }

    @Test
    public void testGetItem() {
        ItemController itemController = new ItemController();
        Item item = new Item();
        item.setName("Test Item");
        itemController.setItem(item);
        assertEquals("Test Item", itemController.getItem().getName());
    }

    @Test
    public void testGetItemById() {
        ItemController itemController = new ItemController();
        Item item = new Item();
        item.setId(1L);
        item.setName("Test Item");
        itemController.setItem(item);
        assertEquals("Test Item", itemController.getItemById(1L).getName());
    }

    @Test
    public void testAddItem() {
        ItemController itemController = new ItemController();
        Item item = new Item();
        item.setName("Test Item");
        itemController.addItem(item);
        assertEquals("Test Item", itemController.getItems().get(0).getName());
    }

    @Test
    public void testUpdateItem() {
        ItemController itemController = new ItemController();
        Item item = new Item();
        item.setId(1L);
        item.setName("Test Item");
        itemController.addItem(item);
        Item updatedItem = new Item();
        updatedItem.setId(1L);
        updatedItem.setName("Updated Test Item");
        itemController.updateItem(updatedItem);
        assertEquals("Updated Test Item", itemController.getItems().get(0).getName());
    }

    @Test
    public void testDeleteItem() {
        ItemController itemController = new ItemController();
        Item item = new Item();
        item.setId(1L);
        item.setName("Test Item");
        itemController.addItem(item);
        itemController.deleteItem(1L);
        assertEquals(0, itemController.getItems().size());
    }
}

