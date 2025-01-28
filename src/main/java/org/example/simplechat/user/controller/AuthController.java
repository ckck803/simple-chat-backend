package org.example.simplechat.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.simplechat.security.dto.UserInfoCreateDto;
import org.example.simplechat.security.dto.UserInfoDto;
import org.example.simplechat.user.service.UserInfoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.management.relation.RoleNotFoundException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserInfoService userInfoService;

    @PostMapping("/api/signup")
    public ResponseEntity createUser(@RequestBody UserInfoCreateDto userInfoCreateDto) throws RoleNotFoundException {
        UserInfoDto userInfo = userInfoService.createUserInfo(userInfoCreateDto);

        if(userInfo == null){
            return ResponseEntity.badRequest().body("회원가입에 실패했습니다.");
        }
        return ResponseEntity.created(null).body("회원가입에 성공 했습니다.");
    }
}
