package com.example.basketballmatching.report.entity;


import com.example.basketballmatching.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class ReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reportId;

    private String content;

    @ManyToOne
    private UserEntity reportUser;

    @ManyToOne
    private UserEntity reportedUser;

    @CreatedDate
    private LocalDateTime createdDateTime;

    private LocalDateTime blackListStartDateTime;

}
