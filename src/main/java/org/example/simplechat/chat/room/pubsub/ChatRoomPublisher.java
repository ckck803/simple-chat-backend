package org.example.simplechat.chat.room.pubsub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.simplechat.chat.room.dto.ChatRoomCreate;
import org.example.simplechat.chat.room.dto.ChatRoomDto;
import org.example.simplechat.chat.room.entity.ChatRoom;
import org.example.simplechat.chat.room.protocol.ChatRoomProtocol;
import org.example.simplechat.chat.room.repository.ChatRoomRepository;
import org.example.simplechat.user.repository.UserInfoRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class ChatRoomPublisher {

    private final ChatRoomRepository chatRoomRepository;

    private final ChannelTopic simpleChatRoomTopic;
    private final RedisTemplate redisTemplate;

    // @Transactional
    // public void createCharRoomToBroker(ChatRoomCreate chatRoomCreate){
    //     ChatRoom chatRoom = ChatRoom.builder()
    //             .roomId(UUID.randomUUID().toString())
    //             .name(chatRoomCreate.getName())
    //             .userCount(0)
    //             .build();
    //
    //     ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
    //     ChatRoomDto chatRoomDto = new ChatRoomDto(savedChatRoom);;
    //     redisTemplate.convertAndSend(simpleChatRoomTopic.getTopic(), chatRoomDto);
    // }

    @Transactional
    public void createCharRoomToBroker(ChatRoomProtocol chatRoomProtocol){
        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(UUID.randomUUID().toString())
                .name(chatRoomProtocol.getName())
                .userCount(0)
                .build();

        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
        ChatRoomDto chatRoomDto = new ChatRoomDto(savedChatRoom);
        chatRoomProtocol.setRoomId(chatRoomDto.getRoomId());
        redisTemplate.convertAndSend(simpleChatRoomTopic.getTopic(), chatRoomProtocol);
    }

    @Transactional
    public void deleteCharRoomToBroker(ChatRoomProtocol chatRoomProtocol){
        chatRoomRepository.deleteByRoomId(chatRoomProtocol.getRoomId());
        redisTemplate.convertAndSend(simpleChatRoomTopic.getTopic(), chatRoomProtocol);
    }
}
