package com.example.basketballmatching.admin.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBlackListEntity is a Querydsl query type for BlackListEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBlackListEntity extends EntityPathBase<BlackListEntity> {

    private static final long serialVersionUID = 1443803535L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBlackListEntity blackListEntity = new QBlackListEntity("blackListEntity");

    public final NumberPath<Integer> blackListId = createNumber("blackListId", Integer.class);

    public final com.example.basketballmatching.user.entity.QUserEntity blackUser;

    public final DatePath<java.time.LocalDate> endDate = createDate("endDate", java.time.LocalDate.class);

    public final DatePath<java.time.LocalDate> startDate = createDate("startDate", java.time.LocalDate.class);

    public QBlackListEntity(String variable) {
        this(BlackListEntity.class, forVariable(variable), INITS);
    }

    public QBlackListEntity(Path<? extends BlackListEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBlackListEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBlackListEntity(PathMetadata metadata, PathInits inits) {
        this(BlackListEntity.class, metadata, inits);
    }

    public QBlackListEntity(Class<? extends BlackListEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.blackUser = inits.isInitialized("blackUser") ? new com.example.basketballmatching.user.entity.QUserEntity(forProperty("blackUser")) : null;
    }

}

