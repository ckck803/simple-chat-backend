package org.example.simplechat.security.login.provider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.simplechat.security.dto.SessionUserInfo;
import org.example.simplechat.security.dto.UserDetailsImpl;
import org.example.simplechat.security.login.token.LoginAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@RequiredArgsConstructor
public class LoginAuthenticationProvider implements AuthenticationProvider {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(authentication.getPrincipal().toString());

        if (isPasswordMatched(authentication.getCredentials().toString(), userDetails.getPassword())) {
            SessionUserInfo sessionUserInfo = new SessionUserInfo();

            sessionUserInfo.setUserId(userDetails.getUserId());
            sessionUserInfo.setEmail(userDetails.getEmail());
            sessionUserInfo.setUsername(userDetails.getUsername());

            return LoginAuthenticationToken.authenticated(sessionUserInfo, null, userDetails.getAuthorities());
        }

        return null;
    }

    private boolean isPasswordMatched(String password, String encodedPassword) {
        return passwordEncoder.matches(password, encodedPassword);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return LoginAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
