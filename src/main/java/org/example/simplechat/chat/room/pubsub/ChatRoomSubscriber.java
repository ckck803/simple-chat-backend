package org.example.simplechat.chat.room.pubsub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.simplechat.chat.room.dto.ChatRoomCreate;
import org.example.simplechat.chat.room.dto.ChatRoomDto;
import org.example.simplechat.chat.room.protocol.ChatRoomProtocol;
import org.example.simplechat.user.entity.UserInfo;
import org.example.simplechat.user.repository.UserInfoRepository;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.lang.runtime.ObjectMethods;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class ChatRoomSubscriber {

    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations operations;

    private final UserInfoRepository userInfoRepository;

    // public void sendToAllUser(String chatRoom) throws JsonProcessingException {
    //     log.info("subscribe chat room ====> {}", chatRoom);
    //
    //     if (!ObjectUtils.isEmpty(chatRoom.isEmpty())) {
    //         ChatRoomDto chatRoomDto = objectMapper.readValue(chatRoom, ChatRoomDto.class);
    //
    //         List<UserInfo> userInfoList = userInfoRepository.findAll();
    //         userInfoList.forEach(userInfo -> {
    //             operations.convertAndSend("/sub/user/" + userInfo.getUserId(), chatRoomDto);
    //         });
    //     }
    // }

    // public void sendToAllUser(String chatRoom) throws JsonProcessingException {
    //     log.info("subscribe chat room ====> {}", chatRoom);
    //
    //     if (!ObjectUtils.isEmpty(chatRoom.isEmpty())) {
    //         ChatRoomDto chatRoomDto = objectMapper.readValue(chatRoom, ChatRoomDto.class);
    //         operations.convertAndSend("/sub/chat/room", chatRoomDto);
    //     }
    // }

    public void sendToAllUser(String chatRoom) throws JsonProcessingException {
        log.info("subscribe chat room ====> {}", chatRoom);

        if (!ObjectUtils.isEmpty(chatRoom.isEmpty())) {
            ChatRoomProtocol chatRoomProtocol = objectMapper.readValue(chatRoom, ChatRoomProtocol.class);
            operations.convertAndSend("/sub/chat/room", chatRoomProtocol);
        }
    }
}
