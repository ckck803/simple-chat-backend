package org.example.simplechat.security.login.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.simplechat.security.dto.LoginUserInfoDto;
import org.example.simplechat.security.dto.SessionUserInfo;
import org.example.simplechat.security.dto.UserDetailsImpl;
import org.example.simplechat.security.jwt.utils.JwtUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
public class LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;
    private final JwtUtils jwtUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Object principal = authentication.getPrincipal();

        if(principal instanceof SessionUserInfo){
            SessionUserInfo sessionUserInfo = (SessionUserInfo) principal;
            String accessToken = jwtUtils.createAccessToken();

            LoginUserInfoDto loginUserInfoDto = new  LoginUserInfoDto();
            loginUserInfoDto.setUserId(sessionUserInfo.getUserId());
            loginUserInfoDto.setEmail(sessionUserInfo.getEmail());
            loginUserInfoDto.setUsername(sessionUserInfo.getUsername());

            response.getWriter().write(objectMapper.writeValueAsString(loginUserInfoDto));
            response.setHeader(HttpHeaders.AUTHORIZATION, accessToken);
            response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
            response.setHeader(HttpHeaders.CACHE_CONTROL,"no-cache");
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        }
    }
}
