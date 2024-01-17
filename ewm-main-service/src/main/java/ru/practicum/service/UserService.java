package ru.practicum.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.dto.user.NewUserRequest;
import ru.practicum.dto.user.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(NewUserRequest newUserRequest);

    List<UserDto> getAllUsers(PageRequest pageRequest, Long[] ids);

    void deleteUser(Long userId);
}
