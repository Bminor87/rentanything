package com.hellmanstudios.rentanything.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.hellmanstudios.rentanything.entities.Category;
import com.hellmanstudios.rentanything.entities.Item;
import com.hellmanstudios.rentanything.entities.User;

import org.springframework.web.bind.annotation.RequestParam;

import com.hellmanstudios.rentanything.repository.ItemRepository;
import com.hellmanstudios.rentanything.repository.RoleRepository;
import com.hellmanstudios.rentanything.repository.UserRepository;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;

import com.hellmanstudios.rentanything.repository.CategoryRepository;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.security.crypto.bcrypt.BCrypt;

@Validated
@Controller
public class WebController {

    private static final Logger log = LoggerFactory.getLogger(WebController.class);

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

    @PostMapping("/profile")
    public String saveProfile(
        @Valid @ModelAttribute User updatedUser,
        BindingResult bindingResult,
        @RequestParam(required = false) String oldPassword,
        @RequestParam(required = false) String newPassword,
        @RequestParam(required = false) String confirmPassword,
        @AuthenticationPrincipal User user,
        Model model) {
        
        log.info("POST request to /profile");

        if (bindingResult.hasErrors()) {
            //model.addAttribute("user", updatedUser);
            return "profile";
        }

        user.setUsername(updatedUser.getUsername());
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
    public String register(Model model) {
        log.info("GET request to /register");

        model.addAttribute("user", new User());

        return "register";
    }
 
    @PostMapping("/register")
    public String postUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model, @RequestParam(required = true) String confirmPassword) {
        log.info("POST request to /register");

        log.info("User: {}", user);

        if (user.getPassword().isEmpty() || !user.getPassword().equals(confirmPassword)) {
            model.addAttribute("user", user);
            model.addAttribute("passwordError", "Password and confirmation do not match.");
            return "register";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            return "register";
        }

        user.setRole(roleRepository.findById(1L).get());

        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));

        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {

            log.error("Constraint violation: {}", e.getMessage());

            if (e.getMessage().contains("(username)")) {
                model.addAttribute("usernameError", "This username is already taken.");
            }
            if (e.getMessage().contains("(email)")) {
                model.addAttribute("emailError", "This email is already registered.");
            }

            model.addAttribute("user", user);

            return "register";
        }

        model.addAttribute("registered", true);

        return "login";
    }
    

    @GetMapping("/login")
    public String login() {
        log.info("GET request to /login");
        return "login";
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public String handleConstraintViolationException(ConstraintViolationException e, Model model) {
        log.error("Constraint violation: {}", e.getMessage());
        model.addAttribute("errorMessage", "There was an error processing your request. Please correct the highlighted fields and try again.");
        return "profile";
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation error");
    }


}
