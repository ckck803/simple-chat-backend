package org.example.simplechat.runner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.simplechat.user.entity.UserRole;
import org.example.simplechat.user.repository.UserRoleRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class InitialRoleRunner implements ApplicationRunner {

    private final UserRoleRepository userRoleRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        UserRole userRole = UserRole.builder().name("USER").build();
        userRoleRepository.save(userRole);
    }
}
