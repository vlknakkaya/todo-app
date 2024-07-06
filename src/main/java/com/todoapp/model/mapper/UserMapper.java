package com.todoapp.model.mapper;

import com.todoapp.model.dto.UserDTO;
import com.todoapp.model.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends DtoMapper<User, UserDTO> {
}
