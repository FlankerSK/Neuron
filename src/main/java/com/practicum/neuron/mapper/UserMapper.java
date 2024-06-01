package com.practicum.neuron.mapper;

import com.practicum.neuron.po.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("SELECT * from users where username = #{username}")
    User findUserFromUsername(@Param("username") String username);
}
