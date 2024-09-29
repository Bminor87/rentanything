package com.hellmanstudios.rentanything.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.hellmanstudios.rentanything.entities.Item;
import com.hellmanstudios.rentanything.entities.Category;
import java.util.List;


public interface ItemRepository extends CrudRepository<Item, Long> {
    List<Item> findByCategory(Category category);

    @Query("SELECT i FROM Item i WHERE i.name LIKE %?1% OR i.description LIKE %?1%")
    List<Item> search(String query);
}
