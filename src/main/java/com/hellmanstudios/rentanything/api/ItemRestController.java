package com.hellmanstudios.rentanything.api;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.hellmanstudios.rentanything.entities.Item;
import com.hellmanstudios.rentanything.entities.Category;
import com.hellmanstudios.rentanything.dto.ItemDTO;
import com.hellmanstudios.rentanything.repository.ItemRepository;
import com.hellmanstudios.rentanything.repository.CategoryRepository;

import jakarta.validation.Valid;

/**
 * REST controller for managing items
 * 
 * @author Jesse Hellman
 * @version 1.0
 */
@RestController
@RequestMapping("/api/items")
@Validated
public class ItemRestController {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ItemRestController(ItemRepository itemRepository, CategoryRepository categoryRepository) {
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
    }

    /**
     * Get all items
     * 
     * @return all items
     */
    @GetMapping
    public ResponseEntity<List<ItemDTO>> getItems() {
        List<Item> items = (List<Item>) itemRepository.findAll();

        if (items.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No items available");
        }

        List<ItemDTO> itemDTOs = items.stream()
            .map(Item::toDTO)
            .toList();

        return ResponseEntity.ok(itemDTOs);
    }

    /**
     * Get an item by id
     * 
     * @param id the id of the item
     * @return the item
     */
    @GetMapping("/{id}")
    public ResponseEntity<ItemDTO> getItem(@PathVariable Long id) {
        Optional<Item> optionalItem = itemRepository.findById(id);

        if (optionalItem.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found");
        }

        ItemDTO itemDTO = optionalItem.get().toDTO();
        return ResponseEntity.ok(itemDTO);
    }

    /**
     * Add a new item
     * 
     * @param itemDTO the item to add
     * @return the added item
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ItemDTO> addItem(@Valid @RequestBody ItemDTO itemDTO) {
        Item item = new Item();
        updateItemFromDTO(item, itemDTO);

        if (itemDTO.category() != null && itemDTO.category().id() != null) {
            Category category = categoryRepository.findById(itemDTO.category().id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category not found"));
            item.setCategory(category);
        }

        Item savedItem = itemRepository.save(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedItem.toDTO());
    }

    /**
     * Update an existing item
     * 
     * @param id      the id of the item to update
     * @param itemDTO the updates for the item
     * @return the updated item
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ItemDTO> updateItem(@PathVariable Long id, @Valid @RequestBody ItemDTO itemDTO) {
        Item item = itemRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));

        updateItemFromDTO(item, itemDTO);

        if (itemDTO.category() != null && itemDTO.category().id() != null) {
            Category category = categoryRepository.findById(itemDTO.category().id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category not found"));
            item.setCategory(category);
        } else {
            item.setCategory(null); // Clear category if not provided
        }

        Item updatedItem = itemRepository.save(item);
        return ResponseEntity.ok(updatedItem.toDTO());
    }

    /**
     * Delete an item by id
     * 
     * @param id the id of the item to delete
     * @return 204 No Content
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        Item item = itemRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));

        itemRepository.delete(item);
        return ResponseEntity.noContent().build();
    }

    /**
     * Update item fields from the given DTO
     * 
     * @param item    the target item entity
     * @param itemDTO the source DTO with data
     */
    private void updateItemFromDTO(Item item, ItemDTO itemDTO) {
        item.setName(itemDTO.name());
        item.setDescription(itemDTO.description());
        item.setPrice(itemDTO.price() != null ? itemDTO.price() : 0.0);
        item.setImage(itemDTO.image());
        item.setAvailable(itemDTO.available());
    }
}
