package com.hellmanstudios.rentanything.dto;

public record ItemDTO(
    Long id,
    String name,
    String description,
    Double price,
    String image,
    Boolean available,
    CategoryDTO category

) {
    
}
