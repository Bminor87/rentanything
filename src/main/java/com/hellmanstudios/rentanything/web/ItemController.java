package com.hellmanstudios.rentanything.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ItemController {
    
    @GetMapping("/admin/items")
    public String items() {
        return "items";
    }
    
}

