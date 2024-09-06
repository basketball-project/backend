package com.example.basketballmatching.admin.entity;

import com.example.basketballmatching.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BlackListEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer blackListId;

    private LocalDate startDate;

    private LocalDate endDate;

    @ManyToOne
    private UserEntity blackUser;

}
