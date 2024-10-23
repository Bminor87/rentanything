package com.hellmanstudios.rentanything.dto;
import java.time.LocalDateTime;

public record RentalDTO (
    Long id,
    Long userId,
    Long itemId,
    LocalDateTime rentalStart,
    LocalDateTime rentalEnd
) {
    
}