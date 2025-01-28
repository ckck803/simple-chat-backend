package org.example.simplechat.chat.message.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.example.simplechat.chat.message.entity.ChatMessage;
import org.example.simplechat.file.dto.SavedFileDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class ChatMessageDto {

    String userId;
    String roomId;
    String message;
    String sender;
    List<SavedFileDto> fileList = new ArrayList<>();

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    LocalDate sendDate;

    @JsonSerialize(using = LocalTimeSerializer.class)
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    LocalTime sendTime;

    // public void setSendTime(String sendTime) {
    //     log.info("sendTime ======> {}", sendTime);
    //     this.sendTime = LocalTime.parse(sendTime);
    // }
    //
    // public void setSendTime(LocalTime sendTime) {
    //     this.sendTime = sendTime;
    // }
    //
    // public void setSendTime(Object sendTime){
    //     log.info("sendTime ======> {}", sendTime);
    //     if(sendTime instanceof LocalTime){
    //         this.sendTime = (LocalTime)sendTime;
    //     }else if(sendTime instanceof String){
    //         this.sendTime = LocalTime.parse((String)sendTime);
    //     }
    // }


    // public void setSendDate(String pattern) {
    //     log.info("pattern =========> {}", pattern);
    //     if (pattern == null || pattern.isEmpty()) {
    //         throw new IllegalArgumentException("sendDate cannot be null or empty");
    //     }
    //
    //     try {
    //         // 먼저 yyyy-MM-dd 형식으로 파싱 시도
    //         DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    //         this.sendDate = LocalDate.parse(pattern, formatter1);
    //     } catch (DateTimeParseException e1) {
    //         try {
    //             // yyyyMMdd 형식으로 파싱 시도
    //             DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyyMMdd");
    //             this.sendDate = LocalDate.parse(pattern, formatter2);
    //         } catch (DateTimeParseException e2) {
    //             throw new IllegalArgumentException("Invalid date format: " + pattern);
    //         }
    //     }
    // }

    // public ChatMessageDto(String userId, String roomId, String message, String sender, List<FileInfoDto> fileList, LocalDate sendDate, LocalTime sendTime) {
    //     this.userId = userId;
    //     this.roomId = roomId;
    //     this.message = message;
    //     this.sender = sender;
    //     this.sendDate = sendDate;
    //     this.sendTime = sendTime;
    //     if (fileList == null || fileList.isEmpty()) {
    //         return;
    //     }
    //     this.fileList = fileList;
    // }
    //
    // public void setFileList(List<FileInfoDto> fileList) {
    //     this.fileList = (fileList == null || fileList.isEmpty()) ? null : fileList;
    // }

    // public ChatMessageDto(String userId, String roomId, String message, String sender, LocalDate sendDate, LocalTime sendTime) {
    //
    //
    //     this.userId = userId;
    //     this.roomId = roomId;
    //     this.message = message;
    //     this.sender = sender;
    //     this.fileList = fileList;
    //     this.sendDate = sendDate;
    //     this.sendTime = sendTime;
    // }
}
