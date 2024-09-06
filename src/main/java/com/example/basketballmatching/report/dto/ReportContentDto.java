package com.example.basketballmatching.report.dto;

import lombok.*;

import java.time.LocalDateTime;

public class ReportContentDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {

        private Integer reportedId;

    }
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {

        private String reportedNickname;

        private String content;

        private LocalDateTime createdDateTime;

        public static Response fromDto(ReportDto reportDto) {

            return Response.builder()
                    .content(reportDto.getContent())
                    .reportedNickname(reportDto.getReportedUser().getNickname())
                    .createdDateTime(reportDto.getCreatedDateTime())
                    .build();
        }

    }


}
