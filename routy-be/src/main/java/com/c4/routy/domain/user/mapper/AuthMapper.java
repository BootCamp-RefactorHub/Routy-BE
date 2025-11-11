package com.c4.routy.domain.user.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthMapper {

    String findEmailByUsernameAndPhone(String username, String phone);
}
