package com.top5nacional.virtualkeyboard.dto;

import com.top5nacional.virtualkeyboard.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    private String username;
    private String jwt;
}
