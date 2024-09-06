package com.example.basketballmatching.gameCreator.dto;

import com.example.basketballmatching.gameCreator.entity.GameEntity;
import com.example.basketballmatching.gameCreator.type.CityName;
import com.example.basketballmatching.gameCreator.type.FieldStatus;
import com.example.basketballmatching.gameCreator.type.Gender;
import com.example.basketballmatching.gameCreator.type.MatchFormat;
import com.example.basketballmatching.user.entity.UserEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import javax.swing.text.html.parser.Entity;
import java.time.LocalDateTime;

public class GameDto {

    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder

    public static class CreateRequest {
        @NotBlank(message = "제목은 필수 입력 값 입니다.")
        @Size(max = 50, message = "제목은 최대 50자 입니다.")
        private String title;

        @NotBlank(message = "내용은 필수 입력 값 입니다.")
        @Size(max = 300, message = "내용은 최대 300자 입니다.")
        private String content;

        @NotNull(message = "인원 수는 필수 입력 값 입니다.")
        private Long headCount;

        @NotNull(message = "실내외는 필수 입력 값 입니다.")
        private FieldStatus fieldStatus;

        @NotNull(message = "성별은 필수 입력 값 입니다.")
        private Gender gender;

        @NotNull(message = "시작 날짜는 필수 입력 값 입니다.")
        private LocalDateTime startDateTime;

        @NotBlank(message = "주소는 필수 입력 값 입니다.")
        @Size(max = 200, message = "주소는 최대 200자 입니다.")
        private String address;

        @NotNull(message = "위도는 필수 입력 값 입니다.")
        private Double latitude;

        @NotNull(message = "경도는 필수 입력 값 입니다.")
        private Double longitude;

        @NotNull(message = "경기 형식은 필수 입력 값 입니다.")
        private MatchFormat matchFormat;

        @NotNull(message =  "도시 이름은 필수 입력 값 입니다.")
        private CityName cityName;

        public static GameEntity toEntity(CreateRequest request, UserEntity user){
            return GameEntity.builder()
                    .title(request.getTitle())
                    .content(request.getContent())
                    .headCount(request.getHeadCount())
                    .fieldStatus(request.getFieldStatus())
                    .gender(request.getGender())
                    .startDateTime(request.getStartDateTime())
                    .address(request.getAddress())
                    .latitude(request.getLatitude())
                    .longitude(request.getLongitude())
                    .matchFormat(request.getMatchFormat())
                    .userEntity(user)
                    .cityName(request.getCityName())
                    .build();
        }
    }
    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateResponse {

        private Integer gameId;

        private String title;

        private String content;

        private Long headCount;

        private FieldStatus fieldStatus;

        private Gender gender;

        private LocalDateTime startDateTime;

        private LocalDateTime createdDateTime;

        private String address;

        private Double latitude;

        private Double longitude;

        private CityName cityName;

        private MatchFormat matchFormat;

        private Integer userId;

        public static CreateResponse toDto(GameEntity gameEntity){
            return CreateResponse.builder()
                    .gameId(gameEntity.getGameId())
                    .title(gameEntity.getTitle())
                    .content(gameEntity.getContent())
                    .headCount(gameEntity.getHeadCount())
                    .fieldStatus(gameEntity.getFieldStatus())
                    .gender(gameEntity.getGender())
                    .startDateTime(gameEntity.getStartDateTime())
                    .createdDateTime(gameEntity.getCreatedDateTime())
                    .address(gameEntity.getAddress())
                    .latitude(gameEntity.getLatitude())
                    .longitude(gameEntity.getLongitude())
                    .cityName(gameEntity.getCityName())
                    .matchFormat(gameEntity.getMatchFormat())
                    .userId(gameEntity.getUserEntity().getUserId())
                    .build();
        }
    }

    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateRequest {
        @NotNull(message = "게임 아이디는 필수 입력 값 입니다.")
        private Long gameId;

        @NotBlank(message = "제목은 필수 입력 값 입니다.")
        @Size(max = 50, message = "제목은 최대 50자 입니다.")
        private String title;

        @NotBlank(message = "내용은 필수 입력 값 입니다.")
        @Size(max = 300, message = "내용은 최대 300자 입니다.")
        private String content;

        @NotNull(message = "인원 수는 필수 입력 값 입니다.")
        private Long headCount;

        @NotNull(message = "실내외는 필수 입력 값 입니다.")
        private FieldStatus fieldStatus;

        @NotNull(message = "성별은 필수 입력 값 입니다.")
        private Gender gender;

        @NotNull(message = "시작 날짜는 필수 입력 값 입니다.")
        private LocalDateTime startDateTime;


        @NotBlank(message = "주소는 필수 입력 값 입니다.")
        @Size(max = 200, message = "주소는 최대 200자 입니다.")
        private String address;

        @NotNull(message = "경기 형식은 필수 입력 값 입니다.")
        private MatchFormat matchFormat;

    }



    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DetailResponse {
        private Integer gameId;

        private String title;

        private String content;

        private Long headCount;

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

        private Integer userId;

        public static  DetailResponse toDto(GameEntity gameEntity) {
            return DetailResponse.builder()
                    .gameId(gameEntity.getGameId())
                    .title(gameEntity.getTitle())
                    .content(gameEntity.getContent())
                    .headCount(gameEntity.getHeadCount())
                    .fieldStatus(gameEntity.getFieldStatus())
                    .gender(gameEntity.getGender())
                    .startDateTime(gameEntity.getStartDateTime())
                    .createdDateTime(gameEntity.getCreatedDateTime())
                    .deletedDateTime(gameEntity.getDeletedDateTime())
                    .address(gameEntity.getAddress())
                    .latitude(gameEntity.getLatitude())
                    .longitude(gameEntity.getLongitude())
                    .cityName(gameEntity.getCityName())
                    .matchFormat(gameEntity.getMatchFormat())
                    .userId(gameEntity.getUserEntity().getUserId())
                    .build();
        }
    }
}
