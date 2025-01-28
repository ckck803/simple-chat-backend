package org.example.simplechat.security.dto;

import lombok.Data;

@Data
public class SessionUserInfo {
    String userId;
    String username;
    String email;
}
