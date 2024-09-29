package com.hellmanstudios.rentanything.repository;

import org.springframework.data.repository.CrudRepository;

import com.hellmanstudios.rentanything.entities.Category;

public interface CategoryRepository extends CrudRepository<Category, Long> {
}
