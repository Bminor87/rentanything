package com.hellmanstudios.rentanything.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CategoryController {
    
    @GetMapping("/admin/categories")
    public String categories() {
        return "categories";
    }

}
