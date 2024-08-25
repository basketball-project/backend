package com.example.basketballmatching.gameUsers.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QParticipantGame is a Querydsl query type for ParticipantGame
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QParticipantGame extends EntityPathBase<ParticipantGame> {

    private static final long serialVersionUID = 1474249869L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QParticipantGame participantGame = new QParticipantGame("participantGame");

    public final DateTimePath<java.time.LocalDateTime> acceptDateTime = createDateTime("acceptDateTime", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> cancelDateTime = createDateTime("cancelDateTime", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> createdDateTime = createDateTime("createdDateTime", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> deleteDateTime = createDateTime("deleteDateTime", java.time.LocalDateTime.class);

    public final com.example.basketballmatching.gameCreator.entity.QGameEntity gameEntity;

    public final NumberPath<Integer> participantId = createNumber("participantId", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> rejectDateTime = createDateTime("rejectDateTime", java.time.LocalDateTime.class);

    public final EnumPath<com.example.basketballmatching.gameUsers.type.ParticipantGameStatus> status = createEnum("status", com.example.basketballmatching.gameUsers.type.ParticipantGameStatus.class);

    public final com.example.basketballmatching.user.entity.QUserEntity userEntity;

    public QParticipantGame(String variable) {
        this(ParticipantGame.class, forVariable(variable), INITS);
    }

    public QParticipantGame(Path<? extends ParticipantGame> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QParticipantGame(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QParticipantGame(PathMetadata metadata, PathInits inits) {
        this(ParticipantGame.class, metadata, inits);
    }

    public QParticipantGame(Class<? extends ParticipantGame> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.gameEntity = inits.isInitialized("gameEntity") ? new com.example.basketballmatching.gameCreator.entity.QGameEntity(forProperty("gameEntity"), inits.get("gameEntity")) : null;
        this.userEntity = inits.isInitialized("userEntity") ? new com.example.basketballmatching.user.entity.QUserEntity(forProperty("userEntity")) : null;
    }

}

