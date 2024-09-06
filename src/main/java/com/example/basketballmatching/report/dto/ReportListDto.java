package com.example.basketballmatching.report.dto;

import com.example.basketballmatching.report.entity.ReportEntity;
import com.example.basketballmatching.user.type.GenderType;
import com.example.basketballmatching.user.type.Position;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportListDto {

    private Integer reportId;

    private String userName;

    private Integer reportedId;

    private GenderType genderType;

    private Position position;

    public static ReportListDto of(ReportEntity reportEntity) {

        return ReportListDto.builder()
                .reportId(reportEntity.getReportUser().getUserId())
                .reportedId(reportEntity.getReportedUser().getUserId())
                .genderType(reportEntity.getReportedUser().getGenderType())
                .userName(reportEntity.getReportedUser().getName())
                .position(reportEntity.getReportedUser().getPosition())
                .build();


    }


}
