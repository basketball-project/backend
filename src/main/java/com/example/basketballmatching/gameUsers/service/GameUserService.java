package com.example.basketballmatching.gameUsers.service;


import com.example.basketballmatching.game.entity.GameEntity;
import com.example.basketballmatching.game.type.CityName;
import com.example.basketballmatching.game.type.FieldStatus;
import com.example.basketballmatching.game.type.Gender;
import com.example.basketballmatching.game.type.MatchFormat;
import com.example.basketballmatching.gameUsers.dto.GameSearchDto;
import com.example.basketballmatching.gameUsers.repository.GameSpecification;
import com.example.basketballmatching.gameUsers.repository.GameUserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GameUserService {

    private final GameUserRepository gameUserRepository;

    public List<GameSearchDto> findFilteredGame(
            LocalDate localDate,
            CityName cityName,
            FieldStatus fieldStatus,
            Gender gender,
            MatchFormat matchFormat) {

        Specification<GameEntity> gameEntitySpec = getGameEntitySpec(localDate, cityName, fieldStatus, gender, matchFormat);


        List<GameEntity> gameListNow = gameUserRepository.findAll(gameEntitySpec);



        return getGameSearch(gameListNow);

    }

    public List<GameSearchDto> searchAddress(String address) {
        List<GameEntity> gameEntities =
                gameUserRepository.findByAddressContainsIgnoreCaseAndStartDateTimeAfterOrderByStartDateTimeAsc(
                        address, LocalDateTime.now()
                );

        return getGameSearch(gameEntities);
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
            List<GameEntity> gameListNow) {

        log.info("경기 리스트 조회 시작");

        List<GameSearchDto> gameList = new ArrayList<>();

        gameListNow.forEach((e) ->
                gameList.add(GameSearchDto.of(e)));

        return  gameList;
    }

}
