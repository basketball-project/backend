package com.example.basketballmatching.admin.repository;

import com.example.basketballmatching.admin.entity.BlackListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface BlackListRepository extends JpaRepository<BlackListEntity, Integer> {


    Optional<BlackListEntity> findByBlackUser_UserIdAndEndDateAfter(
            Integer reportedUserId, LocalDate time
    );

    Optional<BlackListEntity> findByBlackUser_LoginIdAndEndDateAfter(
            String loginId, LocalDate now
    );

}
