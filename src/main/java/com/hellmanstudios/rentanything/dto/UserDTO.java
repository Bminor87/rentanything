package com.hellmanstudios.rentanything.dto;

import com.hellmanstudios.rentanything.entities.Role;

public record UserDTO (
    String username,
    String email,
    String firstName,
    String lastName,
    String phone,
    String address,
    String city,
    String postalCode,
    Role role
) {
}
