package com.top5nacional.virtualkeyboard.controller;

import com.top5nacional.virtualkeyboard.dto.LoginDTO;
import com.top5nacional.virtualkeyboard.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO body) {
        System.out.println("Attempt to POST request at /auth/login");
        return authenticationService.loginUser(body.getUsername(), body.getPassword());
    }

    @PostMapping("/start-session")
    public ResponseEntity<?> startSession() {
        System.out.println("Attempt to POST request at /auth/RequestToken");
        return authenticationService.startSession();
    }

    @GetMapping("/get-session")
    public ResponseEntity<?> getSession(@RequestHeader("Authorization") String bearerToken) {
        System.out.println("Attempt to POST request at /auth/RequestToken");
        return authenticationService.getSession(bearerToken);
    }
}
