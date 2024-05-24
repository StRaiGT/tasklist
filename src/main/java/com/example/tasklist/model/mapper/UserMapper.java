package com.example.tasklist.model.mapper;

import com.example.tasklist.model.dto.UserDto;
import com.example.tasklist.model.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends Mappable<User, UserDto> {
}
