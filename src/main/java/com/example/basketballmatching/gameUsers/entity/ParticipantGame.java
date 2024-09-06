package com.example.basketballmatching.gameUsers.entity;

import com.example.basketballmatching.gameCreator.entity.GameEntity;
import com.example.basketballmatching.gameUsers.type.ParticipantGameStatus;
import com.example.basketballmatching.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ParticipantGame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer participantId;

    @Enumerated(EnumType.STRING)
    private ParticipantGameStatus status;

    @CreatedDate
    private LocalDateTime createdDateTime;

    private LocalDateTime acceptDateTime;

    private LocalDateTime rejectDateTime;

    private LocalDateTime cancelDateTime;

    private LocalDateTime deleteDateTime;

    @ManyToOne
    @JoinColumn(nullable = false)
    private GameEntity gameEntity;

    @ManyToOne
    @JoinColumn(nullable = false)
    private UserEntity userEntity;



}
