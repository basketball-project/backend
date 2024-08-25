package com.example.basketballmatching.gameUsers.service;


import com.example.basketballmatching.auth.security.JwtTokenExtract;
import com.example.basketballmatching.gameCreator.entity.GameEntity;
import com.example.basketballmatching.gameCreator.type.CityName;
import com.example.basketballmatching.gameCreator.type.FieldStatus;
import com.example.basketballmatching.gameCreator.type.Gender;
import com.example.basketballmatching.gameCreator.type.MatchFormat;
import com.example.basketballmatching.gameUsers.dto.GameSearchDto;
import com.example.basketballmatching.gameUsers.dto.ParticipantGameDto;
import com.example.basketballmatching.gameUsers.entity.ParticipantGame;
import com.example.basketballmatching.gameUsers.repository.GameSpecification;
import com.example.basketballmatching.gameUsers.repository.GameUserRepository;
import com.example.basketballmatching.gameUsers.repository.ParticipantGameRepository;
import com.example.basketballmatching.gameUsers.type.ParticipantGameStatus;
import com.example.basketballmatching.global.exception.CustomException;
import com.example.basketballmatching.user.entity.UserEntity;
import com.example.basketballmatching.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
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




    public List<GameSearchDto> findFilteredGame(
            LocalDate localDate,
            CityName cityName,
            FieldStatus fieldStatus,
            Gender gender,
            MatchFormat matchFormat) {

        Specification<GameEntity> gameEntitySpec = getGameEntitySpec(localDate, cityName, fieldStatus, gender, matchFormat);


        List<GameEntity> gameListNow = gameUserRepository.findAll(gameEntitySpec);



        return getGameSearch(gameListNow, null);

    }

    public List<GameSearchDto> searchAddress(String address) {
        List<GameEntity> gameEntities =
                gameUserRepository.findByAddressContainsIgnoreCaseAndStartDateTimeAfterOrderByStartDateTimeAsc(
                        address, LocalDateTime.now()
                );

        Integer userId = null;

        return getGameSearch(gameEntities, userId);
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


    private static Specification<GameEntity> getGameEntitySpec(
            LocalDate localDate,
            CityName cityName,
            FieldStatus fieldStatus,
            Gender gender,
            MatchFormat matchFormat
    ) {

        log.info("경기 검색 시작");
        Specification<GameEntity> specification = Specification.where(
                GameSpecification.startDate(LocalDate.now())
                        .and(GameSpecification.notDeleted())
        );

        if (localDate != null) {
            specification = specification.and(GameSpecification.withDate(localDate)).and(GameSpecification.notDeleted());
        };

        if (cityName != null) {
            specification = specification.and(GameSpecification.withCityName(cityName
            ));
        }

        if (fieldStatus != null) {
            specification = specification.and(GameSpecification.withFieldStatus(fieldStatus));
        }

        if (gender != null) {
            specification = specification.and(GameSpecification.withGender(gender));
        }

        if (matchFormat != null) {
            specification.and(GameSpecification.withMatchFormat(matchFormat));
        }

        return specification;
    }


    private static List<GameSearchDto> getGameSearch(
            List<GameEntity> gameListNow, Integer userId) {

        log.info("경기 리스트 조회 시작");

        List<GameSearchDto> gameList = new ArrayList<>();

        gameListNow.forEach((e) ->
                gameList.add(GameSearchDto.of(e, userId)));

        return  gameList;
    }



}
