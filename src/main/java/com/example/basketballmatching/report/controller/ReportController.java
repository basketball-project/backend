package com.example.basketballmatching.report.controller;

import com.example.basketballmatching.report.dto.CreateReportDto;
import com.example.basketballmatching.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/report")
public class ReportController {

    private final ReportService reportService;


    @PostMapping("/create-report")
    @PreAuthorize("hasRole('ROLE_USER')")
    public CreateReportDto.Response createReport(@RequestBody CreateReportDto.Request request) {

        return CreateReportDto.Response.fromDto(
                reportService.createReport(request.getReportedNickname(), request.getContent())
        );
    }

}
