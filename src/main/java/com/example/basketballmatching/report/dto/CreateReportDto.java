package com.example.basketballmatching.report.dto;

import lombok.*;

import java.time.LocalDateTime;

public class CreateReportDto {


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {

        private String reportedNickname;

        private String content;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {

        private String reportNickname;

        private String content;

        private String reportedNickname;

        private LocalDateTime createdDateTime;

        public static Response fromDto(ReportDto reportDto) {
            return Response.builder()
                    .reportNickname(reportDto.getReportUser().getNickname())
                    .content(reportDto.getContent())
                    .reportedNickname(reportDto.getContent())
                    .createdDateTime(reportDto.getCreatedDateTime())
                    .build();
        }


    }

}
