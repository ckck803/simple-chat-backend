package org.example.simplechat.chat.message.repository.custom;

import org.example.simplechat.chat.message.dto.ChatMessageDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomChatMessageRepository {
    List<ChatMessageDto> findChatMessageListByRoomId(String roomId, Pageable pageable);

    // List<ChatRoomMessageGroupDto> findChatMessageListByRoomId(String roomId);
    // List<ChatRoomMessageGroupDto> findChatMessageListByRoomId(String roomId, Pageable pageable);
    Long findCountChatMessageListByRoomId(String roomId);
}
