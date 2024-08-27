package com.example.basketballmatching.report.service;


import com.example.basketballmatching.auth.security.JwtTokenExtract;
import com.example.basketballmatching.global.exception.CustomException;
import com.example.basketballmatching.report.dto.ReportDto;
import com.example.basketballmatching.report.entity.ReportEntity;
import com.example.basketballmatching.report.repository.ReportRepository;
import com.example.basketballmatching.user.entity.UserEntity;
import com.example.basketballmatching.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

}
