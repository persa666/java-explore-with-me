package ru.practicum.mapper;

import ru.practicum.dto.user.NewUserRequest;
import ru.practicum.dto.user.UserDto;
import ru.practicum.dto.user.UserShortDto;
import ru.practicum.model.User;

public class UserMapper {
    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getName(),
                user.getId(),
                user.getEmail(),
                user.getPermission()
        );
    }

    public static User toUser(NewUserRequest newUserRequest) {
        return new User(
                newUserRequest.getEmail(),
                newUserRequest.getName()
        );
    }

    public static UserShortDto toUserShortDto(User user) {
        return new UserShortDto(
                user.getId(),
                user.getName()
        );
    }
}
