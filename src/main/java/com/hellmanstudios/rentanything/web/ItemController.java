package com.hellmanstudios.rentanything.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.hellmanstudios.rentanything.RentanythingApplication;
import com.hellmanstudios.rentanything.entities.Category;
import com.hellmanstudios.rentanything.entities.Item;
import com.hellmanstudios.rentanything.repository.UserRepository;
import com.hellmanstudios.rentanything.repository.ItemRepository;

import java.security.Principal;

@Controller
public class ItemController {
     
    private static final Logger log = LoggerFactory.getLogger(RentanythingApplication.class);

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public ItemController(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }
    
    @GetMapping("/items")
    public String getItems(Model model) {
        log.info("GET request to /items");

        Iterable<Item> items = itemRepository.findAll();

        model.addAttribute("items", items);

        return "items";
    }

    @GetMapping("/items/{id}")
    public String getItem(@PathVariable Long id, Principal principal, Model model) {

        if (principal != null && userRepository.findByUsername(principal.getName()).hasAuthority("ADMIN")) {
            return "redirect:/items/" + id + "/edit";
        }

        log.info("GET request to /items/{}", id);

        Item item = itemRepository.findById(id).get();

        model.addAttribute("item", item);

        return "item";
    }

    @GetMapping("/items/{id}/edit")
    public String editItem(@PathVariable Long id, Model model) {
        log.info("GET request to /items/{}", id);

        Item item = itemRepository.findById(id).get();

        model.addAttribute("item", item);

        return "edit_item";
    }
    
}

