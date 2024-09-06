package com.example.basketballmatching.gameCreator.repository;

import com.example.basketballmatching.gameCreator.entity.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface GameRepository extends
        JpaRepository<GameEntity, Long> {

    Optional<Long> countByStartDateTimeBetweenAndAddressAndDeletedDateTimeNull(LocalDateTime beforeDatetime, LocalDateTime afterDateTime, String address);

    Optional<GameEntity> findByGameIdAndDeletedDateTimeNull(Integer gameId);


}
