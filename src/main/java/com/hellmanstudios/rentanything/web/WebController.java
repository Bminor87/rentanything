package com.hellmanstudios.rentanything.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.hellmanstudios.rentanything.RentanythingApplication;
import com.hellmanstudios.rentanything.entities.Category;
import com.hellmanstudios.rentanything.entities.Item;

import org.springframework.web.bind.annotation.RequestParam;

import com.hellmanstudios.rentanything.repository.ItemRepository;
import com.hellmanstudios.rentanything.repository.CategoryRepository;

import java.util.List;

@Controller
public class WebController {

    private static final Logger log = LoggerFactory.getLogger(RentanythingApplication.class);

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("*")
    public String fallback() {
        log.info("GET request to somewhere unknown");
        return "redirect:/";
    }

    @GetMapping("/")
    public String index(Model model) {
        log.info("GET request to /");

        List<Item> items = (List<Item>) itemRepository.findAll();
        List<Category> categories = (List<Category>) categoryRepository.findAll();

        model.addAttribute("items", items);
        model.addAttribute("categories", categories);

        for (Category category : categories) {
            log.info("Category: {}", category.getName());
            for (Item item : category.getItems()) {
                log.info("Item: {}", item.getName());
            }
        }

        return "index";
    }

    @GetMapping("/search")
    public String search(@RequestParam String query, Model model) {
        log.info("GET request to /search with query: {}", query);

        List<Item> items = itemRepository.search(query);

        log.info("Found {} items", items.size());
        model.addAttribute("query", query);
        model.addAttribute("items", items);

        return "search_results";
    }

    @GetMapping("/profile")
    public String profile() {
        log.info("GET request to /profile");
        return "profile";
    }

    @GetMapping("/register")
    public String register() {
        log.info("GET request to /register");
        return "register";
    }

    @GetMapping("/login")
    public String login() {
        log.info("GET request to /login");
        return "login";
    }

}
