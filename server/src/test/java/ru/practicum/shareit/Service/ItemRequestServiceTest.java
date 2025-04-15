package ru.practicum.shareit.Service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoRequest;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.request.service.RequestServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemRequestServiceTest {
    @Mock
    private RequestRepository repository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private RequestServiceImpl service;
    private UserDto userDto;
    private User user;
    private ItemRequestDtoRequest newItemRequest;
    private ItemRequest itemRequest;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).name("Vladimir").email("gorych@mail.ru").build();
        userService.createUser(user);
        userDto = UserMapper.mapToUserDto(user);
        newItemRequest = ItemRequestDtoRequest.builder().description("description").build();
        itemRequest = new ItemRequest(1L, newItemRequest.getDescription(), user, LocalDateTime.now());
    }

    @Test
    void createRequest_ShouldReturnItemRequestDto() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(repository.save(any(ItemRequest.class))).thenReturn(itemRequest);
        ItemRequestDto result = service.createRequest(userDto.getId(), newItemRequest);
        assertNotNull(result);
        assertEquals(itemRequest.getId(), result.getId());
        assertEquals(newItemRequest.getDescription(), result.getDescription());
        verify(userRepository).findById(user.getId());
        verify(repository).save(any(ItemRequest.class));
    }

    @Test
    void createRequest_ShouldThrowNotFoundExceptionUser() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            service.createRequest(user.getId(), newItemRequest);
        });
        assertEquals("Пользователь с id = 1 не найден", exception.getMessage());
        verify(userRepository).findById(user.getId());
        verify(repository, never()).save(any(ItemRequest.class));
    }

    @Test
    void getRequestsByRequestorId_ShouldReturnListItemRequestDto() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(repository.findAllByRequestorId(user.getId()))
                .thenReturn(Collections.singletonList(itemRequest));
        List<ItemRequestDto> result = service.getRequestsByRequestorId(user.getId());
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(itemRequest.getId(), result.get(0).getId());
        verify(userRepository).findById(user.getId());
        verify(repository).findAllByRequestorId(user.getId());
    }

    @Test
    void getRequestsByRequestorId_ShouldThrowNotFoundExceptionUser() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            service.getRequestsByRequestorId(user.getId());
        });
        assertEquals("Пользователь с id = 1 не найден", exception.getMessage());
        verify(userRepository).findById(user.getId());
        verify(repository, never()).findAllByRequestorId(anyLong());
    }

    @Test
    void getByRequestId_ShouldReturnItemRequestDto() {
        when(repository.findById(itemRequest.getId())).thenReturn(Optional.of(itemRequest));
        when(itemRepository.findByRequestId(itemRequest.getId())).thenReturn(Collections.emptyList());
        ItemRequestDto result = service.getByRequestId(itemRequest.getId());
        assertNotNull(result);
        assertEquals(itemRequest.getId(), result.getId());
        verify(repository).findById(itemRequest.getId());
        verify(itemRepository).findByRequestId(itemRequest.getId());
    }

    @Test
    void getAllRequests_ShouldReturnListItemRequestDto() {
        when(repository.findAll())
                .thenReturn(Collections.singletonList(itemRequest));
        List<ItemRequestDto> result = service.getAllRequests();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(itemRequest.getId(), result.get(0).getId());
        verify(repository).findAll();
    }
}
