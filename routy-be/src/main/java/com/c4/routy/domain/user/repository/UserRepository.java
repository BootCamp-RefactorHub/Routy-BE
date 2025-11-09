package com.c4.routy.domain.user.repository;


import com.c4.routy.domain.user.entity.UserEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {


    boolean existsByEmail(String email);

    UserEntity findByEmail(String email);
}
