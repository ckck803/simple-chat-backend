package org.example.simplechat.chat.message.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.simplechat.common.jpa.dto.PageInfo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageListDto {

    String roomId;
    List<ChatMessageDto> chatMessageList;
    PageInfo pageInfo;
}
