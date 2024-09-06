package com.example.basketballmatching.gameUsers.repository;


import com.example.basketballmatching.gameCreator.entity.GameEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GameUserRepository extends JpaRepository<GameEntity, Integer> {


    List<GameEntity> findAll(Specification<GameEntity> specification);

    List<GameEntity> findByAddressContainsIgnoreCaseAndStartDateTimeAfterOrderByStartDateTimeAsc(
            String partOfAddress, LocalDateTime currentDateTime
    );

}
