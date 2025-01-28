package org.example.simplechat.chat.room.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ChatRoomListDto {

    List<ChatRoomDto> chatRoomList = new ArrayList<>();
    // Integer totalCount;
    // Integer pageSize;
    // Integer pageNumber;

    public ChatRoomListDto(){

    }

    public ChatRoomListDto(List<ChatRoomDto> chatRoomList){
        this.chatRoomList = chatRoomList;
    }
}
