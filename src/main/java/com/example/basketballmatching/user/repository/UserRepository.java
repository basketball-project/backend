package com.example.basketballmatching.user.repository;


import com.example.basketballmatching.user.entity.UserEntity;
import com.example.basketballmatching.user.type.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {


    boolean existsByEmail(String email);
    boolean existsByLoginId(String loginId);


    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByLoginId(String loginId);

    Optional<UserEntity> findBySocialTypeAndLoginId(SocialType socialType, String id);

}
