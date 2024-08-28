package com.example.basketballmatching.gameCreator.repository;

import com.example.basketballmatching.gameCreator.entity.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.Optional;

public interface GameRepository extends
        JpaRepository<GameEntity, Long> {

    Optional<Long> countByStartDateTimeBetweenAndAddressAndDeletedDateTimeNull(LocalDateTime beforeDatetime, LocalDateTime afterDateTime, String address);

    Optional<GameEntity> findByGameIdAndDeletedDateTimeNull(Long gameId);

    Optional<Long> countByDeletedDateTimeNullAndUserEntityUserId(Long userId);

    Optional<Long> countByStartDateTimeBetweenAndAddressAndDeletedDateTimeNullAndGameIdNot(
            LocalDateTime beforeDatetime, LocalDateTime afterDateTime,
            String address, Long gameId);
}
