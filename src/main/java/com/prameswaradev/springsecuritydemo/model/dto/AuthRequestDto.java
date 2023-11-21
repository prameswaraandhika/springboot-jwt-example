package com.prameswaradev.springsecuritydemo.model.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthRequestDto {
    private String username;
    private String password;


}
