package com.hellmanstudios.rentanything.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.hellmanstudios.rentanything.RentanythingApplication;
import com.hellmanstudios.rentanything.entities.Category;
import com.hellmanstudios.rentanything.repository.CategoryRepository;
import com.hellmanstudios.rentanything.repository.ItemRepository;
import com.hellmanstudios.rentanything.repository.UserRepository;

import jakarta.validation.Valid;

import java.security.Principal;
import java.util.List;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class CategoryController {
        
    private static final Logger log = LoggerFactory.getLogger(RentanythingApplication.class);

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public CategoryController(ItemRepository itemRepository, CategoryRepository categoryRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/categories/create")
    public String createCategory(Model model) {
        log.info("GET request to /categories/create");

        model.addAttribute("editing", false);

        model.addAttribute("category", new Category());

        return "edit_category";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/categories/{id}/edit")
    public String editCategory(@PathVariable Long id, Model model) {
        log.info("GET request to /categories/{}", id);

        Category category = categoryRepository.findById(id).get();

        model.addAttribute("editing", true);

        model.addAttribute("category", category);

        return "edit_category";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("categories")
    public String saveCategory(@Valid @ModelAttribute("category") Category editedCategory, RedirectAttributes redirectAttributes ,Model model) {

        Category category = (editedCategory.getId() != null) 
        ? categoryRepository.findById(editedCategory.getId()).orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + editedCategory.getId())) 
        : new Category();

        category.setName(editedCategory.getName());
        category.setDescription(editedCategory.getDescription());
                
        try {

            categoryRepository.save(category);

        } catch (DataIntegrityViolationException e) {
            
            model.addAttribute("nameError", e.getMessage());

            model.addAttribute("category", editedCategory);
            return "edit_item";
        }
        
        redirectAttributes.addFlashAttribute("message", "category saved successfully!");

        return "redirect:/categories";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/categories/{id}")
    public String deleteCategory(@PathVariable Long id) {
        log.info("DELETE request to /categories/{}", id);

        categoryRepository.deleteById(id);

        return "redirect:/categories";
    }

}
