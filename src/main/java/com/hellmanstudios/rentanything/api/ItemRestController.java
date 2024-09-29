package com.hellmanstudios.rentanything.api;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hellmanstudios.rentanything.repository.ItemRepository;

@RestController
@RequestMapping("/api/items")
public class ItemRestController {
    
    @Autowired
    private ItemRepository itemRepository;

}
