package com.example.basketballmatching.gameCreator.entity;

import com.example.basketballmatching.gameCreator.type.CityName;
import com.example.basketballmatching.gameCreator.type.FieldStatus;
import com.example.basketballmatching.gameCreator.type.Gender;
import com.example.basketballmatching.gameCreator.type.MatchFormat;
import com.example.basketballmatching.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.cglib.core.Local;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
@Entity(name = "game")
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@EntityListeners(AuditingEntityListener.class)
public class GameEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer gameId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Long headCount;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FieldStatus fieldStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    private LocalDateTime createdDate;
    @Column(nullable = false)
    private LocalDateTime startDateTime;

    private LocalDateTime deletedDate;
    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdDateTime;

    private LocalDateTime deletedDateTime;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CityName cityName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MatchFormat matchFormat;

    @ManyToOne
    private UserEntity userEntity;
}
