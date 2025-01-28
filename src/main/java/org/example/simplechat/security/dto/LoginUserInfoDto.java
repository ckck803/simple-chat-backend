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
public class LoginUserInfoDto {
    private String userId;
    private String username;
    private String email;
    private List<String> roleList;
}
