package com.hellmanstudios.rentanything.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import com.hellmanstudios.rentanything.entities.Item;
import com.hellmanstudios.rentanything.entities.Category;
import java.util.List;


public interface ItemRepository extends CrudRepository<Item, Long> {
    List<Item> findByCategory(Category category);

    @Query(value= "SELECT * FROM items WHERE description LIKE :query OR name LIKE :query", nativeQuery = true)
    List<Item> search(@Param("query") String query);
}
