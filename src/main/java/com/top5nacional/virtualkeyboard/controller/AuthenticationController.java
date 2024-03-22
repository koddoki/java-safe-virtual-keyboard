package com.top5nacional.virtualkeyboard.controller;

import com.top5nacional.virtualkeyboard.dto.LoginDTO;
import com.top5nacional.virtualkeyboard.dto.LoginResponseDTO;
import com.top5nacional.virtualkeyboard.dto.RegistrationDTO;
import com.top5nacional.virtualkeyboard.model.Session;
import com.top5nacional.virtualkeyboard.model.User;
import com.top5nacional.virtualkeyboard.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public User registerUser(@RequestBody RegistrationDTO body){
        System.out.println("Attempt to POST request at /auth/register");
        return authenticationService.registerUser(body.getUsername(), body.getPassword());
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginDTO body) {
        System.out.println("Attempt to POST request at /auth/register");
        return authenticationService.loginUserTest(body.getUsername(), body.getPassword());
    }

    @PostMapping("/startSession")
    public ResponseEntity<Session> startSession(@RequestParam String username) {
        System.out.println("Attempt to POST request at /auth/RequestToken");
        Session session = new Session();
        return authenticationService.startSession(username);
    }

    @PostMapping("/getKeyboard")
    public ResponseEntity<Session> getKeyboard(@RequestParam String username) {
        System.out.println("Attempt to POST request at /auth/RequestToken");
        return authenticationService.getKeyboard(username);
    }
}
