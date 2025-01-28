package org.example.simplechat.chat.message.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.simplechat.chat.message.dto.*;
import org.example.simplechat.chat.message.entity.ChatMessage;
import org.example.simplechat.chat.message.repository.ChatMessageRepository;
import org.example.simplechat.chat.room.entity.ChatRoom;
import org.example.simplechat.chat.room.repository.ChatRoomRepository;
import org.example.simplechat.common.jpa.dto.PageInfo;
import org.example.simplechat.user.entity.UserInfo;
import org.example.simplechat.user.repository.UserInfoRepository;
import org.hibernate.query.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatMessageService {

    private final SimpMessageSendingOperations operations;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserInfoRepository userInfoRepository;
    private final RedisTemplate redisTemplate;
    private final ChannelTopic simpleChatTopic;

    // @Transactional
    // public void sendMessageToBroker(ChatMessageDto chatMessageDto) {
    //     saveMessage(chatMessageDto);
    //     operations.convertAndSend("/sub/chatroom/" + chatMessageDto.getRoomId(), chatMessageDto);
    //     log.info("/sub/chatroom/{} send message: {}", chatMessageDto.getRoomId(), chatMessageDto);
    // }

    @Transactional
    public void sendMessageToBroker(ChatMessageDto chatMessageDto) {
        saveMessage(chatMessageDto);
        redisTemplate.convertAndSend(simpleChatTopic.getTopic(), chatMessageDto);
        log.info("/sub/chatroom/{} send message: {}", chatMessageDto.getRoomId(), chatMessageDto);
    }

    public ChatMessage saveMessage(ChatMessageDto chatMessageDto) {
        log.info("message room Id =====> {}", chatMessageDto.getRoomId());
        log.info("message send date =====> {}", chatMessageDto.getSendDate());
        log.info("message send time =====> {}", chatMessageDto.getSendTime());


        Optional<ChatRoom> byRoomId = chatRoomRepository.findByRoomId(chatMessageDto.getRoomId());
        if (byRoomId.isEmpty()) {
            log.error("채팅방을 찾을 수 없습니다.");
            throw new EntityNotFoundException();
        }

        Optional<UserInfo> byUserId = userInfoRepository.findByUserId(chatMessageDto.getUserId());
        if (byUserId.isEmpty()) {
            log.error("사용자를 찾을 수 없습니다.");
            throw new EntityNotFoundException();
        }

        ChatMessage chatMessage = ChatMessage.builder()
                .userInfo(byUserId.get())
                .chatRoom(byRoomId.get())
                .message(chatMessageDto.getMessage())
                .sendDate(chatMessageDto.getSendDate())
                .sendTime(chatMessageDto.getSendTime())
                .build();

        log.info("receive message: {}", chatMessageDto);
        return chatMessageRepository.save(chatMessage);
    }

    public ChatMessageListDto getChatRoomMessages(String roomId, Pageable pageable) {
        ChatMessageListDto chatMessageListDto = new ChatMessageListDto();

        chatMessageListDto.setRoomId(roomId);
        List<ChatMessageDto> chatMessageListByRoomId = chatMessageRepository.findChatMessageListByRoomId(roomId, pageable);
        chatMessageListDto.setChatMessageList(chatMessageListByRoomId);

        Long countChatMessageListByRoomId = chatMessageRepository.findCountChatMessageListByRoomId(roomId);
        PageInfo pageInfo = new PageInfo();
        pageInfo.setTotalElements(countChatMessageListByRoomId);

        return chatMessageListDto;
    }

    // public ChatRoomMessageGroupListDto getChatRoomMessages(String roomId){
    //     ChatRoomMessageGroupListDto chatRoomMessageGroupListDto = new ChatRoomMessageGroupListDto();
    //
    //     // Page page = Page.page(5, 0);
    //     PageRequest pageRequest = PageRequest.of(0, 5);
    //
    //     chatRoomMessageGroupListDto.setRoomId(roomId);
    //     List<ChatRoomMessageDto> chatMessageListByRoomId = chatMessageRepository.findChatMessageListByRoomId(roomId, pageRequest);
    //     chatRoomMessageGroupListDto.setMessageGroupList(chatMessageListByRoomId);
    //
    //     // Long countChatMessageListByRoomId = chatMessageRepository.findCountChatMessageListByRoomId(roomId);
    //     // PageInfo pageInfo = new PageInfo();
    //     // pageInfo.setTotalElements(countChatMessageListByRoomId);
    //
    //     return chatRoomMessageGroupListDto;
    // }
}
