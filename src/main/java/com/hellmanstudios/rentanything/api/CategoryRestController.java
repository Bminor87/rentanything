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

import com.hellmanstudios.rentanything.entities.Category;
import com.hellmanstudios.rentanything.entities.Item;
import com.hellmanstudios.rentanything.dto.CategoryDTO;
import com.hellmanstudios.rentanything.dto.ItemDTO;
import com.hellmanstudios.rentanything.repository.CategoryRepository;
import com.hellmanstudios.rentanything.repository.ItemRepository;

import jakarta.validation.Valid;

/**
 * REST controller for managing categories
 * 
 * @version 1.0
 */
@RestController
@RequestMapping("/api/categories")
@Validated
public class CategoryRestController {

    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public CategoryRestController(CategoryRepository categoryRepository, ItemRepository itemRepository) {
        this.categoryRepository = categoryRepository;
        this.itemRepository = itemRepository;
    }

    /**
     * Get all categories
     * 
     * @return all categories
     */
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getCategories() {
        List<Category> categories = (List<Category>) categoryRepository.findAll();

        if (categories.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No categories available");
        }

        List<CategoryDTO> categoryDTOs = categories.stream()
            .map(Category::toDTO)
            .toList();

        return ResponseEntity.ok(categoryDTOs);
    }

    /**
     * Get a category by id
     * 
     * @param id the id of the category
     * @return the category
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable Long id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);

        if (optionalCategory.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }

        CategoryDTO categoryDTO = optionalCategory.get().toDTO();
        return ResponseEntity.ok(categoryDTO);
    }

    /**
     * Get all items for a specific category
     * 
     * @param id the id of the category
     * @return all items in the category
     */
    @GetMapping("/{id}/items")
    public ResponseEntity<List<ItemDTO>> getItemsByCategory(@PathVariable Long id) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        List<Item> items = itemRepository.findByCategory(category);

        if (items.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No items available for this category");
        }

        List<ItemDTO> itemDTOs = items.stream()
            .map(Item::toDTO)
            .toList();

        return ResponseEntity.ok(itemDTOs);
    }

    /**
     * Add a new category
     * 
     * @param categoryDTO the category to add
     * @return the added category
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CategoryDTO> addCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        Category category = new Category();
        updateCategoryFromDTO(category, categoryDTO);

        Category savedCategory = categoryRepository.save(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory.toDTO());
    }

    /**
     * Update an existing category
     * 
     * @param id          the id of the category to update
     * @param categoryDTO the updates for the category
     * @return the updated category
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        updateCategoryFromDTO(category, categoryDTO);

        Category updatedCategory = categoryRepository.save(category);
        return ResponseEntity.ok(updatedCategory.toDTO());
    }

    /**
     * Delete a category by id
     * 
     * @param id the id of the category to delete
     * @return 204 No Content
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        categoryRepository.delete(category);
        return ResponseEntity.noContent().build();
    }

    /**
     * Update category fields from the given DTO
     * 
     * @param category    the target category entity
     * @param categoryDTO the source DTO with data
     */
    private void updateCategoryFromDTO(Category category, CategoryDTO categoryDTO) {
        category.setName(categoryDTO.name());
        category.setDescription(categoryDTO.description());
    }
}
