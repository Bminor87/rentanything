package com.hellmanstudios.rentanything.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.hellmanstudios.rentanything.entities.Rental;

import java.util.List;

public interface RentalRepository extends CrudRepository<Rental, Long> {

    @Query(value= "SELECT * FROM rentals WHERE user_id = :userId", nativeQuery = true)
    List<Rental> getItemsByUserId(@Param("userId") Long userId);
}
