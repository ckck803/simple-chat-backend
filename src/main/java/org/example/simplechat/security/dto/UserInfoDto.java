package org.example.simplechat.security.dto;

import lombok.Data;
import org.example.simplechat.user.entity.UserInfo;

@Data
public class UserInfoDto {
    String username;
    String email;

    public UserInfoDto(){}

    public UserInfoDto(UserInfo userInfo){
        this.username = userInfo.getUsername();
        this.email = userInfo.getEmail();
    }
}
