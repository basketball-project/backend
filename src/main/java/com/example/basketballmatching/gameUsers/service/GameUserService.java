package com.example.basketballmatching.gameUsers.service;


import com.example.basketballmatching.auth.security.JwtTokenExtract;
import com.example.basketballmatching.gameCreator.entity.GameEntity;
import com.example.basketballmatching.gameCreator.entity.QGameEntity;
import com.example.basketballmatching.gameCreator.type.CityName;
import com.example.basketballmatching.gameCreator.type.FieldStatus;
import com.example.basketballmatching.gameCreator.type.Gender;
import com.example.basketballmatching.gameCreator.type.MatchFormat;
import com.example.basketballmatching.gameUsers.dto.GameSearchDto;
import com.example.basketballmatching.gameUsers.dto.ParticipantGameDto;
import com.example.basketballmatching.gameUsers.entity.ParticipantGame;
import com.example.basketballmatching.gameUsers.repository.GameUserRepository;
import com.example.basketballmatching.gameUsers.repository.ParticipantGameRepository;
import com.example.basketballmatching.gameUsers.type.ParticipantGameStatus;
import com.example.basketballmatching.global.exception.CustomException;
import com.example.basketballmatching.user.entity.UserEntity;
import com.example.basketballmatching.user.repository.UserRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.basketballmatching.gameCreator.type.Gender.FEMALEONLY;
import static com.example.basketballmatching.gameCreator.type.Gender.MALEONLY;
import static com.example.basketballmatching.gameUsers.type.ParticipantGameStatus.ACCEPT;
import static com.example.basketballmatching.gameUsers.type.ParticipantGameStatus.CANCEL;
import static com.example.basketballmatching.global.exception.ErrorCode.*;
import static com.example.basketballmatching.user.type.GenderType.FEMALE;
import static com.example.basketballmatching.user.type.GenderType.MALE;

@Service
@Slf4j
@RequiredArgsConstructor
public class GameUserService {

    private final GameUserRepository gameUserRepository;
    private final JwtTokenExtract jwtTokenExtract;
    private final UserRepository userRepository;
    private final ParticipantGameRepository participantGameRepository;
    private final JPAQueryFactory jpaQueryFactory;

    public Page<GameSearchDto> findFilteredGame(
            LocalDate localDate,
            CityName cityName,
            FieldStatus fieldStatus,
            Gender gender,
            MatchFormat matchFormat, int page, int size) {


        QGameEntity gameEntity = QGameEntity.gameEntity;

        LocalDateTime startOfDay = localDate.atStartOfDay();
        LocalDateTime endOfDay = localDate.atTime(23, 59, 59);

        BooleanBuilder whereClause = new BooleanBuilder();

        query(cityName, fieldStatus, gender, matchFormat, whereClause, gameEntity, startOfDay, endOfDay);

        List<GameEntity> gameListNow = jpaQueryFactory
                .selectFrom(gameEntity)
                .where(whereClause)
                .fetch();

        Integer userId = null;
        return getPageGameSearch(gameListNow, userId, page, size);

    }



    public Page<GameSearchDto> searchAddress(String address, int page, int size) {
        List<GameEntity> gameEntities =
                gameUserRepository.findByAddressContainsIgnoreCaseAndStartDateTimeAfterOrderByStartDateTimeAsc(
                        address, LocalDateTime.now()
                );

        Integer userId = null;

        return getPageGameSearch(gameEntities, userId, page, size);
    }


    public ParticipantGameDto participantGame(Integer gameId) {
        Integer userId = jwtTokenExtract.currentUser().getUserId();

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        GameEntity game = gameUserRepository.findById(gameId)
                .orElseThrow(() -> new CustomException(GAME_NOT_FOUND));


        validateParticipant(user, game);


        gameUserRepository.save(game);

        return ParticipantGameDto.fromEntity(participantGameRepository.save(
                ParticipantGame.builder()
                        .status(ParticipantGameStatus.APPLY)
                        .createdDateTime(LocalDateTime.now())
                        .gameEntity(game)
                        .userEntity(user)
                        .build()
        ));

    }

    public ParticipantGameDto participantCancelGame(Integer gameId) {

        Integer userId = jwtTokenExtract.currentUser().getUserId();

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        GameEntity game = gameUserRepository.findById(gameId)
                .orElseThrow(() -> new CustomException(GAME_NOT_FOUND));

        ParticipantGame participantGame = participantGameRepository.findByUserEntityAndGameEntity(user, game)
                .orElseThrow(() -> new CustomException(PARTICIPANT_NOT_FOUND));

        if (participantGame.getStatus().equals(ACCEPT) && game.getStartDateTime().isBefore(LocalDateTime.now().plusMinutes(10))) {
            throw new CustomException(CANCELLATION_NOT_ALLOWED);
        }

        participantGame.setStatus(CANCEL);
        participantGame.setCancelDateTime(LocalDateTime.now());
        participantGameRepository.save(participantGame);

        return ParticipantGameDto.fromEntity(participantGame);

    }

    private void validateParticipant(UserEntity user, GameEntity game) {
        if (participantGameRepository.existsByUserEntity_UserIdAndGameEntity_GameId(user.getUserId(), game.getGameId())) {
            throw new CustomException(ALREADY_PARTICIPANT_USER);
        }

        if (game.getApplicantNum() >= game.getHeadCount()) {
            throw new CustomException(FULL_PEOPLE_GAME);
        }

        if (game.getStartDateTime().isBefore(LocalDateTime.now())) {
            throw new CustomException(OVER_TIME_GAME);
        }

        if (game.getGender().equals(MALEONLY) && user.getGenderType().equals(FEMALE)) {
            throw new CustomException(ONLY_MALE_GAME);
        }

        if (game.getGender().equals(FEMALEONLY) && user.getGenderType().equals(MALE)) {
            throw new CustomException(ONLY_FEMALE_GAME);
        }
    }





    private static List<GameSearchDto> getGameSearch(
            List<GameEntity> gameListNow, Integer userId) {

        log.info("경기 리스트 조회 시작");

        List<GameSearchDto> gameList = new ArrayList<>();

        gameListNow.forEach((e) ->
                gameList.add(GameSearchDto.of(e, userId)));

        return  gameList;
    }

    private static Page<GameSearchDto> getPageGameSearch(
            List<GameEntity> gameListNow, Integer userId, int page, int size
    ) {

        List<GameSearchDto> gameList = new ArrayList<>();

        gameListNow.forEach(
                (e) -> gameList.add(GameSearchDto.of(e, userId))
        );

        int totalSize = gameList.size();
        int totalPage = (int) Math.ceil((double) totalSize / size);
        int lastPage = totalPage == 0 ? 1 : totalPage;

        page = Math.max(1, Math.min(page, lastPage));

        int start = (page - 1) * size;
        int end = Math.min(page * size, totalSize);

        List<GameSearchDto> pageContent = gameList.subList(start, end);

        PageRequest pageable = PageRequest.of(page - 1, size);

        return new PageImpl<>(pageContent, pageable, totalSize);

    }
    private static void query(CityName cityName, FieldStatus fieldStatus, Gender gender, MatchFormat matchFormat, BooleanBuilder whereClause, QGameEntity gameEntity, LocalDateTime startOfDay, LocalDateTime endOfDay) {
        whereClause.and(gameEntity.deletedDateTime.isNull());

        whereClause.and(gameEntity.createdDateTime.between(startOfDay, endOfDay));

        if (cityName != null) {
            whereClause.and(gameEntity.cityName.eq(cityName));
        }

        if (fieldStatus != null) {
            whereClause.and(gameEntity.fieldStatus.eq(fieldStatus));
        }

        if (gender != null) {
            whereClause.and(gameEntity.gender.eq(gender));
        }

        if (matchFormat != null) {
            whereClause.and(gameEntity.matchFormat.eq(matchFormat));
        }
    }


}
