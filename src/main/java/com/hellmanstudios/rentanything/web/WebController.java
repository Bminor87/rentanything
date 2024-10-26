package com.hellmanstudios.rentanything.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.hellmanstudios.rentanything.RentanythingApplication;
import com.hellmanstudios.rentanything.entities.Category;
import com.hellmanstudios.rentanything.entities.Item;
import com.hellmanstudios.rentanything.entities.User;

import org.springframework.web.bind.annotation.RequestParam;

import com.hellmanstudios.rentanything.repository.ItemRepository;
import com.hellmanstudios.rentanything.repository.RoleRepository;
import com.hellmanstudios.rentanything.repository.UserRepository;
import com.hellmanstudios.rentanything.repository.CategoryRepository;

import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.security.crypto.bcrypt.BCrypt;


@Controller
public class WebController {

    private static final Logger log = LoggerFactory.getLogger(RentanythingApplication.class);

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public WebController(ItemRepository itemRepository, CategoryRepository categoryRepository, UserRepository userRepository, RoleRepository roleRepository) {
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String admin() {
        log.info("GET request to /admin");
        return "admin";
    }

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal User user, Model model) {
        log.info("GET request to /profile");

        model.addAttribute("user", user);

        return "profile";
    }

    @PostMapping("/saveprofile")
    public String saveProfile(
        @AuthenticationPrincipal User user,
        @ModelAttribute User updatedUser,
        @RequestParam(required = false) String oldPassword,
        @RequestParam(required = false) String newPassword,
        @RequestParam(required = false) String confirmPassword,
        Model model) {
        
        log.info("POST request to /saveprofile");
        log.info("User: {}", user);
        log.info("Updated user: {}", updatedUser);

        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setEmail(updatedUser.getEmail());
        user.setPhone(updatedUser.getPhone());
        user.setAddress(updatedUser.getAddress());
        user.setPostalCode(updatedUser.getPostalCode());
        user.setCity(updatedUser.getCity());

        // Update password if requested
        if (!oldPassword.isEmpty() && !newPassword.isEmpty() && !confirmPassword.isEmpty()) {

            if (!BCrypt.checkpw(oldPassword, user.getPassword())) {
                model.addAttribute("passwordError", "Old password is incorrect.");
                return "profile";
            }

            if (!newPassword.equals(confirmPassword)) {
                model.addAttribute("passwordError", "New password and confirmation do not match.");
                return "profile";
            }

            user.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
        }

        userRepository.save(user);
        model.addAttribute("saved", true);

        return "redirect:/profile";
    }


    @GetMapping("/register")
    public String register() {
        log.info("GET request to /register");
        return "register";
    }

    @PostMapping("/register")
    public String postUser(@ModelAttribute User user, Model model) {
        log.info("POST request to /register");

        log.info("User: {}", user);

        user.setRole(roleRepository.findById(1L).get());

        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));

        userRepository.save(user);

        model.addAttribute("registered", true);

        return "redirect:/login";
    }
    

    @GetMapping("/login")
    public String login() {
        log.info("GET request to /login");
        return "login";
    }

}
