package com.example.basketballmatching.report.service;


import com.example.basketballmatching.auth.security.JwtTokenExtract;
import com.example.basketballmatching.global.exception.CustomException;
import com.example.basketballmatching.report.dto.ReportDto;
import com.example.basketballmatching.report.dto.ReportListDto;
import com.example.basketballmatching.report.entity.ReportEntity;
import com.example.basketballmatching.report.repository.ReportRepository;
import com.example.basketballmatching.user.entity.UserEntity;
import com.example.basketballmatching.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.basketballmatching.global.exception.ErrorCode.*;
import static org.springframework.http.HttpStatus.ALREADY_REPORTED;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final UserRepository userRepository;

    private final JwtTokenExtract jwtTokenExtract;

    private final ReportRepository reportRepository;

    public ReportDto createReport(String reportedNickname, String content) {

        Integer reportUserId = jwtTokenExtract.currentUser().getUserId();

        UserEntity reportUser = userRepository.findById(reportUserId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        UserEntity reportedUser = userRepository.findByNicknameAndDeletedAtNull(reportedNickname)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));


        boolean existReport = reportRepository.existsByReportUser_UserIdAndReportedUser_UserId(reportUser.getUserId(), reportedUser.getUserId());

        if (reportedUser == reportUser) {
            throw new CustomException(REPORT_NOT_ALLOWED);
        }

        if (existReport) {
            throw new CustomException(ALREADY_REPORTED_USER);
        }

        return ReportDto.fromEntity(
                reportRepository.save(
                        ReportEntity.builder()
                                .reportUser(reportUser)
                                .reportedUser(reportedUser)
                                .content(content)
                                .createdDateTime(LocalDateTime.now())
                                .build()
                )
        );

    }

    public ReportDto reportContent(Integer reportedId) {

        ReportEntity report = reportRepository.findById(reportedId)
                .orElseThrow(() -> new CustomException(REPORT_NOT_FOUND));


        return ReportDto.fromEntity(report);

    }



    public List<ReportListDto> reportList(int page, int size) {

        Page<ReportEntity> reportListDto = reportRepository.findByBlackListStartDateTimeIsNull(
                PageRequest.of(page, size)
        );

        List<ReportEntity> content = reportListDto.getContent();

        return content.stream().map(ReportListDto::of)
                .collect(Collectors.toList());

    }

}
