package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.dto.user.NewUserRequest;
import ru.practicum.dto.user.UserDto;
import ru.practicum.exp.NonExistentUserException;
import ru.practicum.mapper.UserMapper;
import ru.practicum.model.User;
import ru.practicum.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> getAllUsers(PageRequest pageRequest, Long[] ids) {
        List<Long> idsArray = (ids != null && ids.length == 1) ? new ArrayList<>(List.of(ids)) : new ArrayList<>();
        return userRepository.findByIdIn(idsArray, pageRequest)
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto createUser(NewUserRequest newUserRequest) {
        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(newUserRequest)));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public UserDto replaceSubscriptionParameter(Long userId, Boolean permission) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NonExistentUserException(userId));
        user.setPermission(permission);
        return UserMapper.toUserDto(userRepository.save(user));
    }
}
