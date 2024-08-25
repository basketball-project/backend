package com.example.basketballmatching.gameUsers.dto;

import com.example.basketballmatching.gameCreator.entity.GameEntity;
import com.example.basketballmatching.gameCreator.type.CityName;
import com.example.basketballmatching.gameCreator.type.FieldStatus;
import com.example.basketballmatching.gameCreator.type.Gender;
import com.example.basketballmatching.gameCreator.type.MatchFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class GameSearchDto {

    private Integer gameId;

    private Integer gameOwnerId;

    private Integer myId;

    private String title;

    private String content;

    private Integer headCount;

    private FieldStatus fieldStatus;

    private Gender gender;

    private LocalDateTime startDateTime;

    private LocalDateTime createdDateTime;

    private LocalDateTime deletedDateTime;

    private String address;

    private Double latitude;

    private Double longitude;

    private CityName cityName;

    private MatchFormat matchFormat;

    public static GameSearchDto of(GameEntity gameEntity, Integer userId) {
        return GameSearchDto.builder()
                .gameId(gameEntity.getGameId())
                .gameOwnerId(gameEntity.getUser().getUserId())
                .myId(userId)
                .title(gameEntity.getTitle())
                .content(gameEntity.getContent())
                .headCount(gameEntity.getHeadCount())
                .fieldStatus(gameEntity.getFieldStatus())
                .gender(gameEntity.getGender())
                .startDateTime(gameEntity.getStartDateTime())
                .address(gameEntity.getAddress())
                .latitude(gameEntity.getLatitude())
                .longitude(gameEntity.getLongitude())
                .cityName(gameEntity.getCityName())
                .matchFormat(gameEntity.getMatchFormat())
                .build();


    }

}
