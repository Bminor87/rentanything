package com.hellmanstudios.rentanything.api;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hellmanstudios.rentanything.repository.RentalRepository;

@RestController
@RequestMapping("/api/rentals")
public class RentalRestController {

    @Autowired
    private RentalRepository rentalRepository;

}
