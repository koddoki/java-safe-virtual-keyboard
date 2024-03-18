package com.top5nacional.virtualkeyboard.dto;

import lombok.Data;

import java.util.List;

@Data
public class LoginDTO {
    private String username;
    private List<Integer[]> password;

    private String passwordTemp;
}