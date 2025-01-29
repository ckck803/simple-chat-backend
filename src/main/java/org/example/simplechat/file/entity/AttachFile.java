package org.example.simplechat.file.entity;

import jakarta.persistence.*;
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
    private String contentType;
    private String fileDirectory;
    private String ext;
    private String fileSize;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private AttachFile thumbnailInfo;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private ChatMessage chatMessage;
}
