package org.example.simplechat.chat.message.repository;

import org.example.simplechat.chat.message.entity.ChatMessage;
import org.example.simplechat.chat.message.repository.custom.CustomChatMessageRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long>, CustomChatMessageRepository {
}
