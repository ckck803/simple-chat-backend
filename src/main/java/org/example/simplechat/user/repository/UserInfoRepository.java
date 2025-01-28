package org.example.simplechat.user.repository;

import org.example.simplechat.user.entity.UserInfo;
import org.example.simplechat.user.repository.custom.CustomUserInfoRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long>, CustomUserInfoRepository {
    Optional<UserInfo> findByEmail(String email);
    Optional<UserInfo> findByUserId(String userId);
}
