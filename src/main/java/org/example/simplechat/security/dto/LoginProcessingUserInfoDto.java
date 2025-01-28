package org.example.simplechat.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginProcessingUserInfoDto {
    private String userId;
    private String username;
    private String email;
    private String password;
    private List<String> roleList;
}
