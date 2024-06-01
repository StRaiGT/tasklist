package com.example.tasklist.model.mapper;

import com.example.tasklist.model.dto.UserCreateRequest;
import com.example.tasklist.model.dto.UserResponse;
import com.example.tasklist.model.dto.UserUpdateRequest;
import com.example.tasklist.model.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreateRequest userCreateRequest);

    User toUser(UserUpdateRequest userUpdateRequest);

    UserResponse toUserResponse(User user);
}
