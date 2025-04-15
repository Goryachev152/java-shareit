package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {

    UserDto createUser(User user);

    UserDto updateUser(UpdateUserRequest updateUserRequest, Long userId);

    List<UserDto> getAllUsers();

    UserDto getByIdUser(Long userId);

    UserDto deleteByIdUser(Long userId);
}
