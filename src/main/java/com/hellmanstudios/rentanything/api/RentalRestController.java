package com.hellmanstudios.rentanything.api;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.hellmanstudios.rentanything.entities.Item;
import com.hellmanstudios.rentanything.entities.Rental;
import com.hellmanstudios.rentanything.entities.User;
import com.hellmanstudios.rentanything.dto.RentalDTO;
import com.hellmanstudios.rentanything.repository.ItemRepository;
import com.hellmanstudios.rentanything.repository.RentalRepository;
import com.hellmanstudios.rentanything.repository.UserRepository;

import jakarta.validation.Valid;

/**
 * REST controller for managing rentals
 * 
 * @author Jesse Hellman
 * @version 1.0
 */
@RestController
@RequestMapping("/api/rentals")
@Validated
public class RentalRestController {

    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public RentalRestController(RentalRepository rentalRepository, UserRepository userRepository, ItemRepository itemRepository) {
        this.rentalRepository = rentalRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    /**
     * Get all rentals
     * 
     * @return all rentals
     */
    @GetMapping
    public ResponseEntity<List<RentalDTO>> getRentals() {
        List<Rental> rentals = (List<Rental>) rentalRepository.findAll();

        if (rentals.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No rentals available");
        }

        List<RentalDTO> rentalDTOs = rentals.stream()
            .map(RentalRestController::toDTO)
            .toList();

        return ResponseEntity.ok(rentalDTOs);
    }

    /**
     * Get a rental by id
     * 
     * @param id the id of the rental
     * @return the rental
     */
    @GetMapping("/{id}")
    public ResponseEntity<RentalDTO> getRental(@PathVariable Long id) {
        Rental rental = rentalRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rental not found"));

        return ResponseEntity.ok(toDTO(rental));
    }

    /**
     * Add a new rental
     * 
     * @param rentalDTO the rental to add
     * @return the added rental
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<RentalDTO> addRental(@Valid @RequestBody RentalDTO rentalDTO) {
        User user = userRepository.findById(rentalDTO.userId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));

        Item item = itemRepository.findById(rentalDTO.itemId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Item not found"));

        if (!item.isAvailable()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Item is not available");
        } else {
            item.setAvailable(false);
            itemRepository.save(item);
        }

        Rental rental = new Rental();
        rental.setUser(user);
        rental.setItem(item);
        rental.setRentalStart(rentalDTO.rentalStart() != null ? rentalDTO.rentalStart() : java.time.LocalDateTime.now());
        rental.setRentalEnd(rentalDTO.rentalEnd());

        Rental savedRental = rentalRepository.save(rental);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(savedRental));
    }

    /**
     * Update an existing rental
     * 
     * @param id the id of the rental to update
     * @param rentalDTO the updates for the rental
     * @return the updated rental
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<RentalDTO> updateRental(@PathVariable Long id, @Valid @RequestBody RentalDTO rentalDTO) {
        Rental rental = rentalRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rental not found"));

        User user = userRepository.findById(rentalDTO.userId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));

        Item item = itemRepository.findById(rentalDTO.itemId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Item not found"));

        rental.setUser(user);
        rental.setItem(item);
        rental.setRentalStart(rentalDTO.rentalStart());
        rental.setRentalEnd(rentalDTO.rentalEnd());

        Rental updatedRental = rentalRepository.save(rental);
        return ResponseEntity.ok(toDTO(updatedRental));
    }

    /**
     * Return a rental
     * 
     * @param id
     * @return the returned rental
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/return")
    public ResponseEntity<RentalDTO> returnRental(@PathVariable Long id) {
        Rental rental = rentalRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rental not found"));
            
        if (rental.getRentalEnd() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rental already returned");
        }

        Item item = rental.getItem();
        item.setAvailable(true);
        itemRepository.save(item);

        rental.setRentalEnd(java.time.LocalDateTime.now());

        Rental updatedRental = rentalRepository.save(rental);
        return ResponseEntity.ok(toDTO(updatedRental));
    }

    /**
     * Delete a rental by id
     * 
     * @param id the id of the rental to delete
     * @return 204 No Content
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRental(@PathVariable Long id) {
        Rental rental = rentalRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rental not found"));
            
        Item item = rental.getItem();
        item.setAvailable(true);
        itemRepository.save(item);
        
        rentalRepository.delete(rental);
        return ResponseEntity.noContent().build();
    }

    /**
     * Convert a Rental entity to a RentalDTO
     * 
     * @param rental the rental entity
     * @return the corresponding RentalDTO
     */
    private static RentalDTO toDTO(Rental rental) {
        return new RentalDTO(
            rental.getId(),
            rental.getUser().getId(),
            rental.getItem().getId(),
            rental.getRentalStart(),
            rental.getRentalEnd()
        );
    }
}
