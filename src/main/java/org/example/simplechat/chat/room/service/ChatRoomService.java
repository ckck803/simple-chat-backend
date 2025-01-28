package org.example.simplechat.chat.room.service;

import lombok.RequiredArgsConstructor;
import org.example.simplechat.chat.room.dto.ChatRoomDto;
import org.example.simplechat.chat.room.dto.ChatRoomListDto;
import org.example.simplechat.chat.room.dto.ChatRoomCreate;
import org.example.simplechat.chat.room.entity.ChatRoom;
import org.example.simplechat.chat.room.repository.ChatRoomRepository;
import org.example.simplechat.user.entity.UserInfo;
import org.example.simplechat.user.repository.UserInfoRepository;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserInfoRepository userInfoRepository;

    public ChatRoomListDto getAllChatRoom(){
        List<ChatRoom> all = chatRoomRepository.findAll();
        List<ChatRoomDto> list = all.stream().map(ChatRoomDto::new).toList();
        return new ChatRoomListDto(list);
    }

    public ChatRoomDto getChatRoom(String roomId){
        Optional<ChatRoom> optional = chatRoomRepository.findByRoomId(roomId);

        return optional.map(ChatRoomDto::new).orElse(null);

    }

    public ChatRoomDto createChatRoom(ChatRoomCreate chatRoomCreate){
        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(UUID.randomUUID().toString())
                .name(chatRoomCreate.getName())
                .userCount(0)
                .build();

        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
        return new ChatRoomDto(savedChatRoom);
    }

    public void leaveChatRoom(String roomId){
        // Optional<ChatRoom> optional = chatRoomRepository.findByRoomId(roomId);
        chatRoomRepository.deleteByRoomId(roomId);
    }

    public List<String> getChatMemberIdByRoomId(String roomId){
        return chatRoomRepository.findChatMemberIdListByRoomId(roomId);
    }
}
