package org.example.simplechat.user.repository.custom;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.example.simplechat.security.dto.LoginProcessingUserInfoDto;
import org.example.simplechat.user.entity.QUserInfo;
import org.example.simplechat.user.entity.QUserInfoUserRole;
import org.example.simplechat.user.entity.QUserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

@Repository
public class CustomUserInfoRepositoryImpl implements CustomUserInfoRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private QUserInfo qUserInfo = QUserInfo.userInfo;
    private QUserInfoUserRole qUserInfoUserRole = QUserInfoUserRole.userInfoUserRole;
    private QUserRole qUserRole = QUserRole.userRole;

    @Autowired
    public CustomUserInfoRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(JPQLTemplates.DEFAULT, entityManager);
    }

    @Override
    public Optional<LoginProcessingUserInfoDto> findUserInfo(String email) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(qUserInfo)
                .join(qUserInfo.userRoleList, qUserInfoUserRole)
                .join(qUserInfoUserRole.userRole, qUserRole)
                .where(qUserInfo.email.eq(email))
                .transform(groupBy(qUserInfo.email).as(Projections.constructor(LoginProcessingUserInfoDto.class
                        , qUserInfo.userId
                        , qUserInfo.username
                        , qUserInfo.email
                        , qUserInfo.password
                        , list(Projections.constructor(String.class, qUserRole.name))
                ))).get(email));
    }
}
