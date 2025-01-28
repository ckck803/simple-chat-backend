package org.example.simplechat.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.simplechat.security.dto.UserInfoCreateDto;
import org.example.simplechat.security.dto.UserInfoDto;
import org.example.simplechat.user.entity.UserInfo;
import org.example.simplechat.user.entity.UserInfoUserRole;
import org.example.simplechat.user.entity.UserRole;
import org.example.simplechat.user.repository.UserInfoRepository;
import org.example.simplechat.user.repository.UserInfoUserRoleRepository;
import org.example.simplechat.user.repository.UserRoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.relation.RoleNotFoundException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserInfoService {

    private final PasswordEncoder passwordEncoder;
    private final UserInfoRepository userInfoRepository;
    private final UserInfoUserRoleRepository userInfoUserRoleRepository;
    private final UserRoleRepository userRoleRepository;

    @Transactional
    public UserInfoDto createUserInfo(UserInfoCreateDto userInfoCreateDto) throws RoleNotFoundException {
        Optional<UserInfo> optional = userInfoRepository.findByEmail(userInfoCreateDto.getEmail());
        if(optional.isPresent()){
            log.error("이미 가입된 회원입니다.");
            return null;
        }

        UserInfo userInfo = UserInfo.builder()
                .username(userInfoCreateDto.getUsername())
                .email(userInfoCreateDto.getEmail())
                .password(passwordEncoder.encode(userInfoCreateDto.getPassword()))
                .build();

        UserInfo savedUserInfo = userInfoRepository.save(userInfo);
        Optional<UserRole> role = userRoleRepository.findByName("USER");

        if (role == null){
            throw new RoleNotFoundException();
        }

        UserInfoUserRole userInfoUserRole = UserInfoUserRole.builder()
                .userInfo(savedUserInfo)
                .userRole(role.get())
                .build();

        userInfoUserRoleRepository.save(userInfoUserRole);

        return new UserInfoDto(savedUserInfo);
    }
}
