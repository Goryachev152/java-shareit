package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto createUser(User user) {
        if (isDuplicateEmail(user)) {
            User newUser = userRepository.createUser(user);
            log.info("Пользователь с id {} добавлен в сервис", newUser.getId());
            return UserMapper.mapToUserDto(newUser);
        } else {
            throw new DuplicateEmailException("Этот Email = " + user.getEmail() + " уже занят");
        }
    }

    @Override
    public UserDto updateUser(UpdateUserRequest updateUserRequest, Long userId) {
        User updateUser = validNotFoundUser(userId);
        if (updateUserRequest.getName() != null && updateUserRequest.getEmail() != null) {
            updateUser = updateUser.toBuilder()
                    .id(userId)
                    .name(updateUserRequest.getName())
                    .email(updateUserRequest.getEmail())
                    .build();
        } else if (updateUserRequest.getName() == null && updateUserRequest.getEmail() != null) {
            updateUser = updateUser.toBuilder()
                    .id(userId)
                    .email(updateUserRequest.getEmail())
                    .build();
        } else if (updateUserRequest.getName() != null) {
            updateUser = updateUser.toBuilder()
                    .id(userId)
                    .name(updateUserRequest.getName())
                    .build();
        }
        if (isDuplicateEmailUpdateUser(updateUser, userId)) {
            userRepository.updateUser(updateUser, userId);
            log.info("Пользователь с id {} обновлен", updateUser.getId());
            return UserMapper.mapToUserDto(updateUser);
        } else {
            throw new DuplicateEmailException("Этот Email = " + updateUserRequest.getEmail() + " уже занят");
        }
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.getAllUsers()
                .stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    @Override
    public UserDto getByIdUser(Long userId) {
        User user = validNotFoundUser(userId);
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public UserDto deleteByIdUser(Long userId) {
        User user = validNotFoundUser(userId);
        userRepository.deleteByUserId(userId);
        log.info("Пользователь с id {} удален из сервиса", userId);
        return UserMapper.mapToUserDto(user);
    }

    private boolean isDuplicateEmail(User user) {
        return userRepository.getAllUsers()
                .stream()
                .noneMatch(mapEmail -> mapEmail.equals(user));
    }

    private boolean isDuplicateEmailUpdateUser(User updateUser, Long userId) {
        return userRepository.getAllUsers()
                .stream()
                .filter(user -> !user.getId().equals(userId))
                .noneMatch(user -> user.getEmail().equals(updateUser.getEmail()));
    }

    private User validNotFoundUser(Long userId) {
        Optional<User> user = userRepository.getByIdUser(userId);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
    }
}