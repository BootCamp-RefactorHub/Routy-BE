package com.c4.routy.domain.user.repository;


import com.c4.routy.domain.user.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User,Integer> {

    @Query("SELECT u FROM User u WHERE u.email = :userEmail")
    User findByUserEmail(@Param("userEmail") String userEmail);
}
