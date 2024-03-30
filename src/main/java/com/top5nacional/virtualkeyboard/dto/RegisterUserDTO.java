package com.top5nacional.virtualkeyboard.dto;

import com.top5nacional.virtualkeyboard.model.Role;
import lombok.Data;

import java.util.List;

@Data
public class RegisterUserDTO {
    private String username;
    private String password;
    private List<Role> roles;
}