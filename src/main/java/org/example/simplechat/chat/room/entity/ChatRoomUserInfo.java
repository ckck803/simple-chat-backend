package org.example.simplechat.chat.room.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.simplechat.common.jpa.entity.BaseEntity;
import org.example.simplechat.user.entity.UserInfo;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ChatRoomUserInfo extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private UserInfo userInfo;
}
