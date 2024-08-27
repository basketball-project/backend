package com.example.basketballmatching.report.repository;

import com.example.basketballmatching.report.entity.ReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<ReportEntity, Integer> {


    boolean existsByReportUser_UserIdAndReportedUser_UserId(Integer userId, Integer userId1);
}

