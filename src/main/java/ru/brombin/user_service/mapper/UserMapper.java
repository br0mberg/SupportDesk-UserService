package ru.brombin.user_service.mapper;

import org.mapstruct.Mapper;
import ru.brombin.user_service.dto.UserDto;
import ru.brombin.user_service.entity.User;

@Mapper(componentModel = "cdi")
public interface UserMapper {

    User toEntity(UserDto userDto);

    UserDto toDto(User user);
}
