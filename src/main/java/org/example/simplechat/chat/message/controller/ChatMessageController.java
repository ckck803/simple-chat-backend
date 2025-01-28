package org.example.simplechat.chat.message.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.simplechat.chat.message.dto.ChatMessageDto;
import org.example.simplechat.chat.message.dto.ChatMessageListDto;
import org.example.simplechat.chat.message.service.ChatMessageService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    @MessageMapping("/message")
    // @SendTo("/chat/chatroom")
    public void sendMessage(ChatMessageDto chatMessageDto) {

        chatMessageService.sendMessageToBroker(chatMessageDto);
    }

    @MessageMapping("/chat")
    // @SendTo("/chat/chatroom")
    public void sendMessageToBroker(ChatMessageDto chatMessageDto) {

        chatMessageService.sendMessageToBroker(chatMessageDto);
    }

    @GetMapping("/api/chat/message/{roomId}")
    public ResponseEntity sendMessage(@PathVariable("roomId") String roomId, @PageableDefault(page = 0, size = 5) Pageable pageable, ChatMessageDto chatMessageDto) {
        try {
            ChatMessageListDto chatRoomMessages = chatMessageService.getChatRoomMessages(roomId, pageable);
            return ResponseEntity.ok(chatRoomMessages);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return ResponseEntity.badRequest().body("데이터가 없습니다.");
        }
    }

    // api/chat/room/member/
}
