package org.example.simplechat.user.repository;

import org.example.simplechat.user.entity.UserInfoUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoUserRoleRepository extends JpaRepository<UserInfoUserRole, Long> {
}
