package org.example.simplechat.chat.message.repository.custom;

import com.querydsl.core.annotations.QueryProjection;
import com.querydsl.core.group.GroupByList;
import com.querydsl.core.group.GroupCollector;
import com.querydsl.core.group.GroupExpression;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.NullExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.example.simplechat.chat.message.dto.ChatMessageDto;
import org.example.simplechat.chat.message.entity.QChatMessage;
import org.example.simplechat.chat.room.entity.QChatRoom;
import org.example.simplechat.chat.room.entity.QChatRoomUserInfo;
import org.example.simplechat.file.dto.SavedFileDto;
import org.example.simplechat.file.entity.QAttachFile;
import org.example.simplechat.user.entity.QUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

@Repository
public class CustomChatMessageRepositoryImpl implements CustomChatMessageRepository {

    private JPAQueryFactory jpaQueryFactory;
    private final QChatRoom qChatRoom = QChatRoom.chatRoom;
    private final QChatRoomUserInfo qChatRoomUserInfo = QChatRoomUserInfo.chatRoomUserInfo;
    private final QChatMessage qChatMessage = QChatMessage.chatMessage;
    private final QUserInfo qUserInfo = QUserInfo.userInfo;
    private final QAttachFile qAttachFile = QAttachFile.attachFile;

    public CustomChatMessageRepositoryImpl(@Autowired EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(JPQLTemplates.DEFAULT, entityManager);
    }

    // @Transactional(readOnly = true)
    // @Override
    // public List<ChatMessageDto> findChatMessageListByRoomId(String roomId, Pageable pageable) {
    //     return jpaQueryFactory.select(Projections.constructor(ChatMessageDto.class
    //                     , qUserInfo.userId
    //                     , qChatRoom.roomId
    //                     , qChatMessage.message
    //                     , qUserInfo.username
    //                     , qChatMessage.sendDate
    //                     , qChatMessage.sendTime
    //             ))
    //             .orderBy(qChatMessage.sendDate.desc(), qChatMessage.sendTime.desc())
    //             .offset(pageable.getOffset())
    //             .limit(pageable.getPageSize())
    //             .from(qChatMessage)
    //             .join(qChatMessage.chatRoom, qChatRoom)
    //             .join(qChatMessage.userInfo, qUserInfo)
    //             .where(qChatRoom.roomId.eq(roomId))
    //             .fetch();
    // }

    // @Transactional(readOnly = true)
    // @Override
    // public List<ChatMessageDto> findChatMessageListByRoomId(String roomId, Pageable pageable) {
    //
    //     return jpaQueryFactory.selectFrom(qChatMessage)
    //             .join(qChatMessage.chatRoom, qChatRoom)
    //             .join(qChatMessage.userInfo, qUserInfo)
    //             .leftJoin(qChatMessage.attachFileList, qAttachFile)
    //             .where(qChatRoom.roomId.eq(roomId))
    //             .orderBy(qChatMessage.sendDate.desc(), qChatMessage.sendTime.desc())
    //             .offset(pageable.getOffset())
    //             .limit(pageable.getPageSize())
    //             .transform(groupBy(qChatMessage.id).list(Projections.fields(ChatMessageDto.class
    //                     , qUserInfo.userId
    //                     , qChatRoom.roomId
    //                     , qChatMessage.message
    //                     , qUserInfo.username.as("sender")
    //                     , list(qAttachFile.savedFileName).as("urlList") // 기본값 설정
    //                     // , list(Projections.constructor(String.class, qAttachFile.savedFileName))
    //                     // , list(Projections.constructor(String.class, qAttachFile.fileDirectory.coalesce("").concat(qAttachFile.savedFileName).coalesce("")))
    //                     , qChatMessage.sendDate
    //                     , qChatMessage.sendTime
    //             )));
    // }


    @Transactional(readOnly = true)
    @Override
    public List<ChatMessageDto> findChatMessageListByRoomId(String roomId, Pageable pageable) {

        List<ChatMessageDto> chatMessageDtoList = jpaQueryFactory.selectFrom(qChatMessage)
                .join(qChatMessage.chatRoom, qChatRoom)
                .join(qChatMessage.userInfo, qUserInfo)
                .leftJoin(qChatMessage.attachFileList, qAttachFile)
                .where(qChatRoom.roomId.eq(roomId))
                .orderBy(qChatMessage.sendDate.desc(), qChatMessage.sendTime.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .transform(groupBy(qChatMessage.id).list(Projections.fields(ChatMessageDto.class
                        , qUserInfo.userId.as("userId")
                        , qChatRoom.roomId.as("roomId")
                        , qChatMessage.message.as("message")
                        , qUserInfo.username.as("sender")
                        , list(Projections.fields(SavedFileDto.class,
                                qAttachFile.savedFileName.as("fileName")
                                , qAttachFile.fileType.as("fileType")
                                , qAttachFile.fileDirectory.as("fileDirectory")
                                , qAttachFile.fileSize.as("fileSize")
                        )).as("fileList")
                        , qChatMessage.sendDate.as("sendDate")
                        , qChatMessage.sendTime.as("sendTime")
                )));

        chatMessageDtoList.forEach(chatMessageDto -> {
            if(chatMessageDto.getFileList().size() == 1 && chatMessageDto.getFileList().get(0).getFileName() == null){
                chatMessageDto.setFileList(new ArrayList<>());
            }
        });

        return chatMessageDtoList;
    }


    // @Transactional(readOnly = true)
    // @Override
    // public List<ChatMessageDto> findChatMessageListByRoomId(String roomId, Pageable pageable) {
    //
    //     return jpaQueryFactory.selectFrom(qChatMessage)
    //             .join(qChatMessage.chatRoom, qChatRoom)
    //             .join(qChatMessage.userInfo, qUserInfo)
    //             .leftJoin(qChatMessage.attachFileList, qAttachFile)
    //             .where(qChatRoom.roomId.eq(roomId))
    //             .orderBy(qChatMessage.sendDate.desc(), qChatMessage.sendTime.desc())
    //             .offset(pageable.getOffset())
    //             .limit(pageable.getPageSize())
    //             .transform(groupBy(qChatMessage.id).list(Projections.bean(ChatMessageDto.class
    //                     , qUserInfo.userId.as("userId")
    //                     , qChatRoom.roomId.as("roomId")
    //                     , qChatMessage.message.as("message")
    //                     , qUserInfo.username.as("sender")
    //                     , qChatMessage.sendDate.as("sendDate")
    //                     , qChatMessage.sendTime.as("sendTime")
    //                     , list(
    //                             Expressions.list(FileInfoDto.class
    //                                     , qAttachFile.savedFileName.as("fileName")
    //                                     , qAttachFile.fileType.as("fileType")
    //                                     , qAttachFile.fileDirectory.as("fileDirectory"))
    //                     ).as("fileList")
    //             )));
    // }

    @Override
    public Long findCountChatMessageListByRoomId(String roomId) {
        return jpaQueryFactory.select(qChatMessage.count())
                .from(qChatMessage)
                .join(qChatMessage.chatRoom, qChatRoom)
                .join(qChatMessage.userInfo, qUserInfo)
                .where(qChatRoom.roomId.eq(roomId))
                .fetchOne();
    }
}
