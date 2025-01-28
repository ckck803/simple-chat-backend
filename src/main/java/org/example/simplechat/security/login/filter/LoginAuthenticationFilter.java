package org.example.simplechat.security.login.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.simplechat.security.dto.LoginRequest;
import org.example.simplechat.security.login.token.LoginAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;

@Slf4j
public class LoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper;

    public LoginAuthenticationFilter(String defaultFilterProcessesUrl, ObjectMapper objectMapper) {
        super(defaultFilterProcessesUrl);
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        LoginRequest loginRequest = objectMapper.readValue(request.getReader(), LoginRequest.class);

        LoginAuthenticationToken loginAuthenticationToken = LoginAuthenticationToken
                .beforeAuthenticated(loginRequest);

        return getAuthenticationManager().authenticate(loginAuthenticationToken);
    }
}
