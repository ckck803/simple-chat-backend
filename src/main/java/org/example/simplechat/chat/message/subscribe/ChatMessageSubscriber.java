package org.example.simplechat.chat.message.subscribe;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.simplechat.chat.message.dto.ChatMessageDto;
import org.example.simplechat.chat.room.service.ChatRoomService;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatMessageSubscriber {

    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations operations;
    private final ChatRoomService chatRoomService;

    public void sendToUser(String chatMessage) throws JsonProcessingException {
        ChatMessageDto chatMessageDto = objectMapper.readValue(chatMessage, ChatMessageDto.class);

        // operations.convertAndSend("/user/" + chatMessageDto.getRoomId(), chatMessage);
        operations.convertAndSend("/sub/chatroom/" + chatMessageDto.getRoomId(), chatMessageDto);
    }
}
