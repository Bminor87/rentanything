package com.hellmanstudios.rentanything.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.hellmanstudios.rentanything.RentanythingApplication;
import com.hellmanstudios.rentanything.entities.Category;
import com.hellmanstudios.rentanything.entities.Item;
import com.hellmanstudios.rentanything.repository.UserRepository;
import com.hellmanstudios.rentanything.repository.CategoryRepository;
import com.hellmanstudios.rentanything.repository.ItemRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import java.util.UUID;
import java.util.List;





@Controller
public class ItemController {
     
    private static final Logger log = LoggerFactory.getLogger(RentanythingApplication.class);

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;

    @Value("${upload.dir}")
    private String UPLOAD_DIR;

    public ItemController(ItemRepository itemRepository, CategoryRepository categoryRepository) {
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/items")
    public String getItems(Model model) {
        log.info("GET request to /items");

        Iterable<Item> items = itemRepository.findAll();

        model.addAttribute("items", items);

        return "items";
    }

    @GetMapping("/items/{id}")
    public String getItem(@PathVariable Long id, Model model) {

        log.info("GET request to /items/{}", id);

        Item item = itemRepository.findById(id).get();

        model.addAttribute("item", item);

        return "item";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/items/{id}/edit")
    public String editItem(@PathVariable Long id, Model model) {
        log.info("GET request to /items/{}", id);

        Item item = itemRepository.findById(id).get();

        List<Category> categories = (List<Category>) categoryRepository.findAll();

        model.addAttribute("editing", true);
        model.addAttribute("item", item);
        model.addAttribute("categories", categories);

        return "edit_item";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/items/create")
    public String createItem(Model model) {
        log.info("GET request to /items/create");

        List<Category> categories = (List<Category>) categoryRepository.findAll();

        model.addAttribute("editing", false);
        model.addAttribute("item", new Item());
        model.addAttribute("categories", categories);

        return "edit_item";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/items")
    public String saveItem(@ModelAttribute Item editedItem, @RequestParam(required = false) MultipartFile uploadedImage) {
        log.info("POST request to /items");

        Item item = new Item();

        if (editedItem.getId() != null) {
            item = itemRepository.findById(editedItem.getId()).orElseThrow(() -> new IllegalArgumentException("Invalid item ID"));
        }

        item.setName(editedItem.getName());
        item.setDescription(editedItem.getDescription());
        item.setImage(editedItem.getImage());
        item.setPrice(editedItem.getPrice());
        item.setCategory(editedItem.getCategory());

        // Handle the uploaded image
        if (uploadedImage != null && !uploadedImage.isEmpty()) {

            String imageName = saveUploadedImage(uploadedImage);
            if (imageName != null) {
                item.setImage(imageName);
            } else {
                log.warn("Failed to save uploaded image.");
            }

        } else {
            log.warn("No image uploaded or the file was empty.");
        }

        itemRepository.save(item);

        return "redirect:/items";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/items/{id}")
    public String deleteItem(@PathVariable Long id) {
        log.info("DELETE request to /items/{}", id);

        itemRepository.deleteById(id);

        return "redirect:/items";
    }

    private String saveUploadedImage(MultipartFile uploadedImage) {


        File directory = new File(UPLOAD_DIR);

        if (!directory.exists()) {
            directory.mkdirs(); // Probably unnecessary after trial and error where the files go
        }
        
        // Create a unique filename
        String filename = UUID.randomUUID().toString() + "_" + uploadedImage.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR, filename);
        
        try {
            Files.copy(uploadedImage.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save uploaded image", e);
            //log.error("Error saving file: {}", e.getMessage());
           //return "default.png";
        }
    }
    
    
}

