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
        private String title;

        private String content;

        private Long headCount;

        private FieldStatus fieldStatus;

        private Gender gender;

        private LocalDateTime startDateTime;

        private LocalDateTime createdDateTime;

        private String address;

        private CityName cityName;

        private MatchFormat matchFormat;

        public static CreateResponse toDto(GameEntity gameEntity){
            return CreateResponse.builder()
                    .title(gameEntity.getTitle())
                    .content(gameEntity.getContent())
                    .headCount(gameEntity.getHeadCount())
                    .fieldStatus(gameEntity.getFieldStatus())
                    .gender(gameEntity.getGender())
                    .startDateTime(gameEntity.getStartDateTime())
                    .createdDateTime(gameEntity.getCreatedDateTime())
                    .address(gameEntity.getAddress())
                    .cityName(gameEntity.getCityName())
                    .matchFormat(gameEntity.getMatchFormat())
                    .build();
        }
    }

}
