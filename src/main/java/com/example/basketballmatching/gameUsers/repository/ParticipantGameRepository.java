package com.example.basketballmatching.gameUsers.repository;

import com.example.basketballmatching.gameUsers.entity.ParticipantGame;
import com.example.basketballmatching.gameUsers.type.ParticipantGameStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipantGameRepository extends JpaRepository<ParticipantGame, Integer> {

     boolean existsByUserEntity_UserIdAndGameEntity_GameId(Integer userId, Integer gameId);
}

