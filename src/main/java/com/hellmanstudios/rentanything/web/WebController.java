package com.hellmanstudios.rentanything.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.hellmanstudios.rentanything.RentanythingApplication;
import com.hellmanstudios.rentanything.entities.Item;

import org.springframework.web.bind.annotation.RequestParam;

import com.hellmanstudios.rentanything.repository.ItemRepository;

import java.util.List;

@Controller
public class WebController {

    private static final Logger log = LoggerFactory.getLogger(RentanythingApplication.class);

    @Autowired
    private ItemRepository itemRepository;

    @GetMapping("*")
    public String fallback() {
        log.info("GET request to somewhere unknown");
        return "redirect:/";
    }

    @GetMapping("/")
    public String index(Model model) {
        log.info("GET request to /");
        // TODO: Add categories to the model
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
