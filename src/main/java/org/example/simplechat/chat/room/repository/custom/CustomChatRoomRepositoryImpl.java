package org.example.simplechat.chat.room.repository.custom;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.simplechat.chat.message.dto.ChatMessageDto;
import org.example.simplechat.chat.message.entity.QChatMessage;
import org.example.simplechat.chat.room.entity.QChatRoom;
import org.example.simplechat.chat.room.entity.QChatRoomUserInfo;
import org.example.simplechat.user.entity.QUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.querydsl.core.group.GroupBy.groupBy;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CustomChatRoomRepositoryImpl implements CustomChatRoomRepository {

    private JPAQueryFactory jpaQueryFactory;
    private final QChatRoom qChatRoom = QChatRoom.chatRoom;
    private final QChatRoomUserInfo qChatRoomUserInfo = QChatRoomUserInfo.chatRoomUserInfo;
    private final QChatMessage qChatMessage = QChatMessage.chatMessage;
    private final QUserInfo qUserInfo = QUserInfo.userInfo;

    @Autowired
    public CustomChatRoomRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(JPQLTemplates.DEFAULT, entityManager);
    }

    @Transactional(readOnly = true)
    public List<ChatMessageDto> findChatMessageListByRoomId(String roomId) {
        return jpaQueryFactory.selectFrom(qChatRoom)
                .join(qChatRoom.chatMessageList, qChatMessage)
                .join(qChatMessage.userInfo, qUserInfo)
                .where(qChatRoom.roomId.eq(roomId))
                .transform(groupBy(qChatMessage.chatRoom).list(Projections.constructor(ChatMessageDto.class
                        , qUserInfo.userId
                        , qChatRoom.roomId
                        , qChatMessage.message
                        , qUserInfo.username
                        , qChatMessage.sendDate
                        , qChatMessage.sendTime
                )));
    }

    @Transactional(readOnly = true)
    @Override
    public List<String> findChatMemberIdListByRoomId(String roomId) {
        return jpaQueryFactory.select(Projections.constructor(String.class, qChatRoomUserInfo.userInfo.userId))
                .from(qChatRoomUserInfo)
                .join(qChatRoomUserInfo.chatRoom, qChatRoom)
                .join(qChatRoomUserInfo.userInfo, qUserInfo)
                .where(qChatRoom.roomId.eq(roomId))
                .fetch();

    }
}
