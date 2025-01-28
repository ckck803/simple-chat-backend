package org.example.simplechat.chat.room.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.simplechat.chat.room.dto.ChatRoomCreate;
import org.example.simplechat.chat.room.dto.ChatRoomDto;
import org.example.simplechat.chat.room.dto.ChatRoomListDto;
import org.example.simplechat.chat.room.protocol.ChatRoomProtocol;
import org.example.simplechat.chat.room.protocol.ChatRoomProtocolType;
import org.example.simplechat.chat.room.pubsub.ChatRoomPublisher;
import org.example.simplechat.chat.room.service.ChatRoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatRoomMessageController {

    private final ChatRoomPublisher chatRoomPublisher;

    // @MessageMapping("/chat/room")
    // public void createChatRoom(ChatRoomCreate chatRoomCreate) {
    //     chatRoomPublisher.createCharRoomToBroker(chatRoomCreate);
    // }

    @MessageMapping("/chat/room")
    public void createChatRoom(ChatRoomProtocol chatRoomProtocol) {
        log.info("chatRoomProtocol ===> {}", chatRoomProtocol);

        if(chatRoomProtocol.getRequestType().equals(ChatRoomProtocolType.CREATE.getValue())) {
            chatRoomPublisher.createCharRoomToBroker(chatRoomProtocol);
        }else if(chatRoomProtocol.getRequestType().equals( ChatRoomProtocolType.DELETE.getValue())){
            chatRoomPublisher.deleteCharRoomToBroker(chatRoomProtocol);
        }
    }
}
