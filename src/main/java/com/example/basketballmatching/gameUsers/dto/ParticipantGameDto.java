package com.example.basketballmatching.gameUsers.dto;


import com.example.basketballmatching.gameCreator.entity.GameEntity;
import com.example.basketballmatching.gameUsers.entity.ParticipantGame;
import com.example.basketballmatching.gameUsers.type.ParticipantGameStatus;
import com.example.basketballmatching.user.entity.UserEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParticipantGameDto {

    private Integer participantId;

    private ParticipantGameStatus status;

    private LocalDateTime createdDateTime;

    private LocalDateTime acceptDateTime;

    private LocalDateTime rejectDateTime;

    private LocalDateTime cancelDateTime;

    private LocalDateTime deleteDateTime;

    private GameEntity gameEntity;

    private UserEntity userEntity;


    public static ParticipantGameDto fromEntity(ParticipantGame participantGame) {

        return ParticipantGameDto.builder()
                .participantId(participantGame.getParticipantId())
                .status(participantGame.getStatus())
                .createdDateTime(participantGame.getCreatedDateTime())
                .acceptDateTime(participantGame.getAcceptDateTime())
                .rejectDateTime(participantGame.getRejectDateTime())
                .cancelDateTime(participantGame.getCancelDateTime())
                .deleteDateTime(participantGame.getDeleteDateTime())
                .gameEntity(participantGame.getGameEntity())
                .userEntity(participantGame.getUserEntity())
                .build();

    }

}
