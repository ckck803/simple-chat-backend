package org.example.simplechat.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.simplechat.chat.room.entity.ChatRoomUserInfo;
import org.example.simplechat.common.jpa.entity.BaseEntity;

import java.util.List;
import java.util.UUID;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserInfo extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String userId;
    private String username;
    private String email;
    private String password;

    @OneToMany(mappedBy = "userInfo", fetch = FetchType.LAZY)
    private List<UserInfoUserRole> userRoleList;

    @OneToMany(mappedBy = "userInfo", fetch = FetchType.LAZY)
    private List<ChatRoomUserInfo> chatRoomList;

    @PrePersist
    private void setUserId(){
        this.userId = UUID.randomUUID().toString();
    }
}
