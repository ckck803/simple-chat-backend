package org.example.simplechat.chat.room.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.simplechat.chat.message.entity.ChatMessage;
import org.example.simplechat.common.jpa.entity.BaseEntity;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ChatRoom extends BaseEntity {

    private String roomId;
    private String name;
    private Integer userCount;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "chatRoom")
    private List<ChatRoomUserInfo> userInfoList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "chatRoom")
    private List<ChatMessage> chatMessageList;
}
