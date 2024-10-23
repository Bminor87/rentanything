package com.hellmanstudios.rentanything.repository;

import org.springframework.data.repository.CrudRepository;
import com.hellmanstudios.rentanything.entities.User;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
}
