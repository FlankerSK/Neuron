package com.practicum.neuron.mapper;

import com.practicum.neuron.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    User findUserFromUsername(@Param("username") String username);
}
