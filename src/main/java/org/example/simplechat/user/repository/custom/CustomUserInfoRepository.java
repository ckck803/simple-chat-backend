package org.example.simplechat.user.repository.custom;

import org.example.simplechat.security.dto.LoginProcessingUserInfoDto;

import java.util.Optional;

public interface CustomUserInfoRepository {

    Optional<LoginProcessingUserInfoDto> findUserInfo(String email);
}
