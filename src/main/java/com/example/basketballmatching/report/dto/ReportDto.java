package com.example.basketballmatching.report.dto;


import com.example.basketballmatching.report.entity.ReportEntity;
import com.example.basketballmatching.user.entity.UserEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportDto {

    private Integer reportId;

    private String content;

    private UserEntity reportUser;

    private UserEntity reportedUser;

    private LocalDateTime createdDateTime;

    public static ReportDto fromEntity(ReportEntity report) {
        return ReportDto.builder()
                .reportId(report.getReportId())
                .content(report.getContent())
                .reportUser(report.getReportUser())
                .reportedUser(report.getReportedUser())
                .createdDateTime(report.getCreatedDateTime())
                .build();
    }


}
