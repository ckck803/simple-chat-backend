package org.example.simplechat.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.simplechat.security.dto.LoginProcessingUserInfoDto;
import org.example.simplechat.security.dto.UserDetailsImpl;
import org.example.simplechat.user.repository.UserInfoRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServicesImpl implements UserDetailsService {

    private final UserInfoRepository userInfoRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<LoginProcessingUserInfoDto> optional = userInfoRepository.findUserInfo(email);
        if (optional.isEmpty()) {
            log.error("사용자를 찾을 수 없습니다.");
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }

        LoginProcessingUserInfoDto loginProcessingUserInfoDto = optional.get();

        return UserDetailsImpl.builder()
                .userId(loginProcessingUserInfoDto.getUserId())
                .username(loginProcessingUserInfoDto.getUsername())
                .email(loginProcessingUserInfoDto.getEmail())
                .password(loginProcessingUserInfoDto.getPassword())
                .roleList(loginProcessingUserInfoDto.getRoleList())
                .build();
    }
}
