package ru.practicum.shareit.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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

    @Transactional
    @Override
    public UserDto createUser(User user) {
            User newUser = userRepository.save(user);
            log.info("Пользователь с id {} добавлен в сервис", newUser.getId());
            return UserMapper.mapToUserDto(newUser);
    }

    @Transactional
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
            userRepository.save(updateUser);
            log.info("Пользователь с id {} обновлен", updateUser.getId());
            return UserMapper.mapToUserDto(updateUser);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    @Override
    public UserDto getByIdUser(Long userId) {
        User user = validNotFoundUser(userId);
        return UserMapper.mapToUserDto(user);
    }

    @Transactional
    @Override
    public UserDto deleteByIdUser(Long userId) {
        User user = validNotFoundUser(userId);
        userRepository.delete(user);
        log.info("Пользователь с id {} удален из сервиса", userId);
        return UserMapper.mapToUserDto(user);
    }

    private User validNotFoundUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
    }
}