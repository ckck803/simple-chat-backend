package org.example.simplechat.file.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.simplechat.chat.message.dto.ChatMessageDto;
import org.example.simplechat.chat.message.entity.ChatMessage;
import org.example.simplechat.common.jpa.entity.BaseEntity;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class AttachFile extends BaseEntity {

    private String originalFileName;
    private String savedFileName;
    private String fileType;
    private String fileDirectory;
    private String ext;
    private String fileSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private ChatMessage chatMessage;
}
