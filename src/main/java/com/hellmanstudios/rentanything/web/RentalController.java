package com.hellmanstudios.rentanything.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.hellmanstudios.rentanything.RentanythingApplication;
import com.hellmanstudios.rentanything.entities.Rental;
import com.hellmanstudios.rentanything.repository.RentalRepository;

@Controller
public class RentalController {
    
    private static final Logger log = LoggerFactory.getLogger(RentanythingApplication.class);

    private final RentalRepository rentalRepository;

    public RentalController(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }
    
    @GetMapping("/rentals")
    public String getRentals(Model model) {
        log.info("GET request to /rentals");

        Iterable<Rental> rentals = rentalRepository.getItemsByUserId(1L);

        model.addAttribute("rentals", rentals);

        return "rentals";
    }

}
