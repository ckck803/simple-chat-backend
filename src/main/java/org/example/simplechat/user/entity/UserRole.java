package org.example.simplechat.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.simplechat.common.jpa.entity.BaseEntity;

import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserRole extends BaseEntity {
    private String name;

    @OneToMany(mappedBy = "userRole", fetch = FetchType.LAZY)
    private List<UserInfoUserRole> userRoleList;
}
