package com.top5nacional.virtualkeyboard.controller;

import com.top5nacional.virtualkeyboard.dto.LoginDTO;
import com.top5nacional.virtualkeyboard.dto.RegisterUserDTO;
import com.top5nacional.virtualkeyboard.model.Session;
import com.top5nacional.virtualkeyboard.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterUserDTO body){
        System.out.println("Attempt to POST request at /auth/register");
        return userService.registerUser(body.getUsername(), body.getPassword());
    }
}
