package com.example.basketballmatching.admin.service;

import com.example.basketballmatching.admin.dto.BlackListDto;
import com.example.basketballmatching.admin.entity.BlackListEntity;
import com.example.basketballmatching.admin.repository.BlackListRepository;
import com.example.basketballmatching.auth.security.JwtTokenExtract;
import com.example.basketballmatching.global.exception.CustomException;
import com.example.basketballmatching.global.exception.ErrorCode;
import com.example.basketballmatching.report.entity.ReportEntity;
import com.example.basketballmatching.report.repository.ReportRepository;
import com.example.basketballmatching.user.entity.UserEntity;
import com.example.basketballmatching.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.example.basketballmatching.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class BlackListService {

    private final BlackListRepository blackListRepository;

    private final UserRepository userRepository;

    private final ReportRepository reportRepository;

    private final JwtTokenExtract jwtTokenExtract;


    public void saveBlackList(BlackListDto request) {

        ReportEntity report = reportRepository.findById(request.getReportedId())
                .orElseThrow(() -> new CustomException(ErrorCode.REPORT_NOT_FOUND));

        report.setBlackListStartDateTime(LocalDateTime.now());
        reportRepository.save(report);

        Integer userId = jwtTokenExtract.currentUser().getUserId();

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        UserEntity reportedUser = userRepository.findById(report.getReportedUser().getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Optional<BlackListEntity> alreadyBlackUser = blackListRepository.findByBlackUser_UserIdAndEndDateAfter(reportedUser.getUserId(), LocalDate.now());

        if (alreadyBlackUser.isEmpty()) {
            blackListRepository.save(BlackListEntity.builder()
                    .blackUser(reportedUser)
                    .startDate(LocalDate.now())
                    .endDate(LocalDate.now().plusDays(10))
                    .build());
        } else {
            throw new CustomException(ALREADY_BLACK_USER);
        }




    }


    public void getBlackList(String loginId) {

        blackListRepository.findByBlackUser_LoginIdAndEndDateAfter(loginId, LocalDate.now())
                .orElseThrow(() -> new CustomException(NOT_BLACKLIST));

    }

    public void checkBlackList(String loginId) {

        Optional<BlackListEntity> user = blackListRepository.findByBlackUser_LoginIdAndEndDateAfter(
                loginId, LocalDate.now()
        );

        if (user.isEmpty()) {
            return;
        }

        int compareTo = user.get().getEndDate()
                .compareTo(LocalDate.now());

        if (compareTo > 0) {
            throw new CustomException(BAN_FOR_10DAYS);
        }

    }

}
