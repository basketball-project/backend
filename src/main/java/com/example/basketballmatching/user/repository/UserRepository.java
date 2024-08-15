package com.example.basketballmatching.user.repository;


import com.example.basketballmatching.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {


    boolean existsByEmailAndDeletedAtNull(String email);
    boolean existsByLoginIdAndDeletedAtNull(String loginId);


    Optional<UserEntity> findByEmailAndDeletedAtNull(String email);

    Optional<UserEntity> findByLoginIdAndDeletedAtNull(String loginId);


}
