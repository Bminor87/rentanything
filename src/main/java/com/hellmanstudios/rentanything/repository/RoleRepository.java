package com.hellmanstudios.rentanything.repository;

import org.springframework.data.repository.CrudRepository;

import com.hellmanstudios.rentanything.entities.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {
}
