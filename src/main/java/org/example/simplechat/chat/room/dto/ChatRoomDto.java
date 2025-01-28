package org.example.simplechat.chat.room.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.simplechat.chat.room.entity.ChatRoom;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ChatRoomDto {
    private String roomId;
    private String name;
    private Integer userCount;

    public ChatRoomDto (ChatRoom chatRoom){
        this.roomId = chatRoom.getRoomId();
        this.name = chatRoom.getName();
        this.userCount = chatRoom.getUserCount();
    }
}