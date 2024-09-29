package com.hellmanstudios.rentanything.repository;

import org.springframework.data.repository.CrudRepository;
import com.hellmanstudios.rentanything.entities.Rental;

public interface RentalRepository extends CrudRepository<Rental, Long> {
}
