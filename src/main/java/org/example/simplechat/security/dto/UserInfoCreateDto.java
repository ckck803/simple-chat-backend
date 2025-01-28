package org.example.simplechat.security.dto;

import lombok.Data;

@Data
public class UserInfoCreateDto {
    String username;
    String email;
    String password;
}
