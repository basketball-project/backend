package com.example.basketballmatching.gameUsers.dto;

import com.example.basketballmatching.gameUsers.type.ParticipantGameStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

public class UserJoinGameDto {


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {

        @NotNull(message = "게임 아이디를 입력해주세요.")
        private Integer gameId;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {

        private Integer userId;

        private Integer gameId;

        private String gameAddress;

        private ParticipantGameStatus status;

        private LocalDateTime createdDateTime;

        public static Response from(ParticipantGameDto participantGameDto) {

            return Response.builder()
                    .userId(participantGameDto.getUserEntity().getUserId())
                    .gameId(participantGameDto.getGameEntity().getGameId())
                    .gameAddress(participantGameDto.getGameEntity().getAddress())
                    .status(participantGameDto.getStatus())
                    .createdDateTime(participantGameDto.getCreatedDateTime())
                    .build();
        }

    }

}
