package com.hellmanstudios.rentanything.api;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hellmanstudios.rentanything.repository.UserRepository;

@RestController
@RequestMapping("/api/users")
public class UserRestController {
    
    @Autowired
    private UserRepository userRepository;

}
