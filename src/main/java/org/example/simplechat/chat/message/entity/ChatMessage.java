package org.example.simplechat.chat.message.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.simplechat.chat.message.entity.enums.ChatMessageType;
import org.example.simplechat.chat.room.entity.ChatRoom;
import org.example.simplechat.common.jpa.converter.LocalDateToStringConverter;
import org.example.simplechat.common.jpa.converter.LocalTimeToStringConverter;
import org.example.simplechat.common.jpa.entity.BaseEntity;
import org.example.simplechat.file.entity.AttachFile;
import org.example.simplechat.user.entity.UserInfo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ChatMessage extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private UserInfo userInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private ChatRoom chatRoom;

    // @Enumerated(value = EnumType.STRING)
    // private ChatMessageType chatMessageType;

    @Column(columnDefinition = "longtext")
    private String message;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "chatMessage")
    private List<AttachFile> attachFileList;

    // @Column(columnDefinition = "DATE")
    @Convert(converter = LocalDateToStringConverter.class)
    private LocalDate sendDate;

    // @Column(columnDefinition = "TIME(3)")
    @Convert(converter = LocalTimeToStringConverter.class)
    private LocalTime sendTime;
}
