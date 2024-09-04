package com.example.basketballmatching.report.repository;

import com.example.basketballmatching.report.dto.ReportListDto;
import com.example.basketballmatching.report.entity.ReportEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<ReportEntity, Integer> {


    boolean existsByReportUser_UserIdAndReportedUser_UserId(Integer userId, Integer userId1);

    Page<ReportEntity> findByBlackListStartDateTimeIsNull(Pageable pageable);
}

