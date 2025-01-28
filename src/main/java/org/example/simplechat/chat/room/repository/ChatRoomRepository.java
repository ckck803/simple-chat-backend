package org.example.simplechat.chat.room.repository;

import org.example.simplechat.chat.room.entity.ChatRoom;
import org.example.simplechat.chat.room.repository.custom.CustomChatRoomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>, CustomChatRoomRepository {
    Optional<ChatRoom> findByRoomId(String roomId);
    void deleteByRoomId(String roomId);
}
