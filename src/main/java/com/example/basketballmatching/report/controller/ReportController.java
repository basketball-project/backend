package com.example.basketballmatching.report.controller;

import com.example.basketballmatching.report.dto.CreateReportDto;
import com.example.basketballmatching.report.dto.ReportContentDto;
import com.example.basketballmatching.report.dto.ReportListDto;
import com.example.basketballmatching.report.entity.ReportEntity;
import com.example.basketballmatching.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/report-list")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ReportListDto>> reportList(
            @RequestParam int page,
            @RequestParam int size
    ) {
        return ResponseEntity.ok()
                .body(reportService.reportList(page, size));
    }


    @GetMapping("/report-content")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ReportContentDto.Response> reportContent(
            @RequestParam @Validated Integer reportedId
    ) {
        return ResponseEntity.ok(ReportContentDto.Response.fromDto(
                reportService.reportContent(reportedId)
        ));
    }

}
