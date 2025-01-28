package org.example.simplechat.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.simplechat.security.jwt.utils.JwtUtils;
import org.example.simplechat.security.login.filter.LoginAuthenticationFilter;
import org.example.simplechat.security.login.handler.LoginAuthenticationSuccessHandler;
import org.example.simplechat.security.login.provider.LoginAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private String loginProcessingUrl = "/api/login";
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;
    private final JwtUtils jwtUtils;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(registry -> {
                    registry.anyRequest().permitAll();

                })
                .addFilterBefore(characterEncodingFilter(), CsrfFilter.class)
                .addFilterBefore(loginAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "DELETE", "PUT", "PATCH"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(Arrays.asList("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public CharacterEncodingFilter characterEncodingFilter(){
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        return filter;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public LoginAuthenticationProvider loginAuthenticationProvider(){
        return new LoginAuthenticationProvider(passwordEncoder(), userDetailsService);
    }

    @Bean
    public AuthenticationManager authenticationManager(){
        return new ProviderManager(List.of(loginAuthenticationProvider()));
    }

    @Bean
    public LoginAuthenticationSuccessHandler loginAuthenticationSuccessHandler(){
        return new LoginAuthenticationSuccessHandler(objectMapper, jwtUtils);
    }

    @Bean
    public LoginAuthenticationFilter loginAuthenticationFilter(){
        LoginAuthenticationFilter loginAuthenticationFilter = new LoginAuthenticationFilter(loginProcessingUrl, objectMapper);
        loginAuthenticationFilter.setAuthenticationManager(authenticationManager());
        loginAuthenticationFilter.setAuthenticationSuccessHandler(loginAuthenticationSuccessHandler());
        return loginAuthenticationFilter;
    }
}
