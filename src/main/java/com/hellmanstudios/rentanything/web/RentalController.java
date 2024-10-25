package com.hellmanstudios.rentanything.web;

import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.security.access.prepost.PreAuthorize;

import com.hellmanstudios.rentanything.RentanythingApplication;
import com.hellmanstudios.rentanything.entities.Item;
import com.hellmanstudios.rentanything.entities.Rental;
import com.hellmanstudios.rentanything.entities.User;
import com.hellmanstudios.rentanything.dto.RentalDTO;
import com.hellmanstudios.rentanything.repository.ItemRepository;
import com.hellmanstudios.rentanything.repository.RentalRepository;
import com.hellmanstudios.rentanything.repository.UserRepository;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.security.core.annotation.AuthenticationPrincipal; // One way to get the current user
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.validation.Valid;

import java.security.Principal; // Another way to get the current user

import java.util.List;
import java.util.ArrayList;


@Controller
public class RentalController {
    
    private static final Logger log = LoggerFactory.getLogger(RentanythingApplication.class);

    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public RentalController(RentalRepository rentalRepository, UserRepository userRepository, ItemRepository itemRepository) {
        this.rentalRepository = rentalRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }
    
    @GetMapping("/rentals")
    public String getRentals(Principal principal, Model model) { // Using Principal injection just to remind myself of a way to get the current user
        log.info("GET request to /rentals");

        Iterable<Rental> rentals = rentalRepository.getItemsByUserId(userRepository.findByUsername(principal.getName()).getId());

        model.addAttribute("rentals", rentals);

        return "rentals";
    }

    @PostMapping("/rentals")
    public String postRental(@ModelAttribute RentalDTO rentalDTO, @AuthenticationPrincipal User user) { // Using @AuthenticationPrincipal just to remind myself of an alternative way to get the current user
        log.info("POST request to /rentals");

        Item item = itemRepository.findById(rentalDTO.itemId()).get();

        LocalDateTime rentalStart = LocalDateTime.now();

        Rental rental = new Rental(user, item, rentalStart, null);

        rentalRepository.save(rental);

        item.setAvailable(false);
        itemRepository.save(item);
         
        return "redirect:/rentals";
    }

    @PostMapping("/rentals/{id}/return")
    public String returnRental(@PathVariable Long id) {
        log.info("POST request to /rentals/" + id + "/return");

        Rental rental = rentalRepository.findById(id).get();

        rental.setRentalEnd(LocalDateTime.now());

        Item item = rental.getItem();
        item.setAvailable(true);
        itemRepository.save(item);

        rentalRepository.save(rental);

        return "redirect:/rentals";
    }

    @DeleteMapping("/rentals/{id}")
    public String deleteRental(@PathVariable Long id) {
        log.info("DELETE request to /rentals/" + id);

        Rental rental = rentalRepository.findById(id).get();

        Item item = rental.getItem();
        item.setAvailable(true);
        itemRepository.save(item);

        rentalRepository.deleteById(id);

        return "redirect:/rentals";
    }
    

}
