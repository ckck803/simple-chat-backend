package org.example.simplechat.chat.room.repository.custom;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CustomChatRoomRepository {
    @Transactional(readOnly = true)
    List<String> findChatMemberIdListByRoomId(String roomId);
}
