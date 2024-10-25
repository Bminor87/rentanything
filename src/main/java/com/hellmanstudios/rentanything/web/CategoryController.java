package com.hellmanstudios.rentanything.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.hellmanstudios.rentanything.RentanythingApplication;
import com.hellmanstudios.rentanything.entities.Category;
import com.hellmanstudios.rentanything.repository.CategoryRepository;
import com.hellmanstudios.rentanything.repository.ItemRepository;

import java.util.List;

@Controller
public class CategoryController {
        
    private static final Logger log = LoggerFactory.getLogger(RentanythingApplication.class);

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;

    public CategoryController(ItemRepository itemRepository, CategoryRepository categoryRepository) {
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/categories")
    public String getCategories(Model model) {
        log.info("GET request to /categories");

        List<Category> categories = (List<Category>) categoryRepository.findAll();

        model.addAttribute("categories", categories);

        return "categories";
    }

    @GetMapping("/categories/{id}")
    public String getCategory(@PathVariable Long id, Model model) {
        log.info("GET request to /categories/{}", id);

        Category category = categoryRepository.findById(id).get();

        model.addAttribute("category", category);

        return "category";
    }

    @GetMapping("/categories/{id}/edit")
    public String editCategory(@PathVariable Long id, Model model) {
        log.info("GET request to /categories/{}", id);

        Category category = categoryRepository.findById(id).get();

        model.addAttribute("category", category);

        return "edit_category";
    }

}
