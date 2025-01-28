package org.example.simplechat.chat.room.controller;

import lombok.RequiredArgsConstructor;
import org.example.simplechat.chat.message.dto.ChatMessageDto;
import org.example.simplechat.chat.room.dto.ChatRoomDto;
import org.example.simplechat.chat.room.dto.ChatRoomListDto;
import org.example.simplechat.chat.room.dto.ChatRoomCreate;
import org.example.simplechat.chat.room.service.ChatRoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat/room")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @GetMapping("")
    public ResponseEntity getAllChatRoom(){
        ChatRoomListDto allChatRoom = chatRoomService.getAllChatRoom();
        return ResponseEntity.ok(allChatRoom);
    }

    @PostMapping("")
    public ResponseEntity createChatRoom(@RequestBody ChatRoomCreate chatRoomCreate){
        ChatRoomDto chatRoomDto = chatRoomService.createChatRoom(chatRoomCreate);
        return ResponseEntity.ok(chatRoomDto);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity createChatRoom(@PathVariable(value = "roomId") String roomId){
        ChatRoomDto chatRoomDto = chatRoomService.getChatRoom(roomId);
        return ResponseEntity.ok(chatRoomDto);
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity deleteChatRoom(@PathVariable(value = "roomId") String roomId){
        chatRoomService.leaveChatRoom(roomId);
        return ResponseEntity.ok("채팅방을 삭제 했습니다.");
    }

    @GetMapping("/member/{roomId}")
    public ResponseEntity getChatRoomMemberId(@PathVariable(value = "roomId") String roomId){
        List<String> chatMemberIdByRoomId = chatRoomService.getChatMemberIdByRoomId(roomId);
        return ResponseEntity.ok(chatMemberIdByRoomId);
    }
}
