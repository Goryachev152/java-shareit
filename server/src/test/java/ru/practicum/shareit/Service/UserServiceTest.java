package ru.practicum.shareit.Service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;
    private UserDto userDto;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1L, "Vladimir", "gorych@mail.ru");
        userDto = UserMapper.mapToUserDto(user);
    }

    @Test
    void createUser_ShouldReturnUserDto() {
        when(userRepository.save(any())).thenReturn(user);
        UserDto result = userService.createUser(user);
        assertNotNull(result);
        assertEquals(userDto.getId(), result.getId());
        assertEquals(userDto.getEmail(), result.getEmail());
    }

    @Test
    void updateUser_ShouldReturnUserDto() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);
        userService.createUser(user);
        UpdateUserRequest updatedUserRequest = UpdateUserRequest.builder().name("gorych").email("gorych152@mail.ru").build();
        UserDto result = userService.updateUser(updatedUserRequest, user.getId());
        assertNotNull(result);
        assertEquals(updatedUserRequest.getName(), result.getName());
        assertEquals(updatedUserRequest.getEmail(), result.getEmail());
    }

    @Test
    void updateUser_ShouldThrowNotFoundExceptionUser() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        UpdateUserRequest updatedUserRequest = UpdateUserRequest.builder().name("gorych").email("gorych152@mail.ru").build();
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            userService.updateUser(updatedUserRequest, user.getId());
        });
        assertEquals("Пользователь с id = 1 не найден", exception.getMessage());
    }

    @Test
    void getByIdUser_ShouldReturnUserDto() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        UserDto result = userService.getByIdUser(user.getId());
        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    void getByIdUser_ShouldThrowNotFoundExceptionUser() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            userService.getByIdUser(2L);
        });
        assertEquals("Пользователь с id = 2 не найден", exception.getMessage());
    }

    @Test
    void deleteUser() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        userService.deleteByIdUser(user.getId());
        verify(userRepository).findById(user.getId());
        verify(userRepository).delete(user);
    }
}
