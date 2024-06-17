package com.practicum.neuron.mapper;

import com.practicum.neuron.entity.account.LoginUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LoginUserMapper {
    @Select("SELECT * from users where username = #{username}")
    LoginUser findUserFromUsername(@Param("username") String username);
}
