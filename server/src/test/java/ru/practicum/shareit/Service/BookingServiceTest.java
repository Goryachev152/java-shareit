package ru.practicum.shareit.Service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private BookingServiceImpl bookingService;
    private User user;
    private User owner;
    private Item item;
    private BookingDtoRequest bookingDtoRequest;
    private Booking booking;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).name("Vladimir").email("gorych@mail.ru").build();
        owner = User.builder().id(2L).name("Vladimir2").email("gorych152@mail.ru").build();
        item = Item.builder().id(2L).name("name").description("description").owner(owner).available(true).build();
        bookingDtoRequest = BookingDtoRequest.builder()
                .itemId(2L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusMinutes(10))
                .build();
        booking = BookingMapper.mapToBooking(bookingDtoRequest, user, item);
        booking.setStatus(Status.WAITING);
    }

    @Test
    void addBooking_ShouldReturnBookingDto() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        BookingDto result = bookingService.addBooking(user.getId(), bookingDtoRequest);
        assertNotNull(result);
        assertEquals(booking.getId(), result.getId());
        assertEquals(booking.getItem().getId(), result.getItem().getId());
        verify(userRepository).findById(user.getId());
        verify(itemRepository).findById(item.getId());
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void addBooking_ShouldThrowNotFoundExceptionUser() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            bookingService.addBooking(user.getId(), bookingDtoRequest);
        });
        assertEquals("Пользователь с id = 1 не найден", exception.getMessage());
        verify(itemRepository, never()).findById(anyLong());
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void addBooking_ShouldThrowNotFoundExceptionItem() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.empty());
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            bookingService.addBooking(user.getId(), bookingDtoRequest);
        });
        assertEquals("Вещь с id = 2 не найдена", exception.getMessage());
        verify(userRepository).findById(user.getId());
        verify(itemRepository).findById(item.getId());
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void addBooking_ShouldThrowValidationExceptionItem() {
        item.setAvailable(false);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            bookingService.addBooking(user.getId(), bookingDtoRequest);
        });
        assertEquals("Вещь с id 2 недоступна для бронирования", exception.getMessage());
        verify(userRepository).findById(user.getId());
        verify(itemRepository).findById(item.getId());
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void findBookingByUser_ShouldReturnListBooking() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(bookingRepository.findByBookerIdOrderByStartDesc(user.getId())).thenReturn(Collections.singletonList(booking));
        List<BookingDto> result = bookingService.findBookingItemByUserId(user.getId(), "ALL");
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(booking.getId(), result.get(0).getId());
        verify(userRepository).findById(user.getId());
        verify(bookingRepository).findByBookerIdOrderByStartDesc(user.getId());
    }

    @Test
    void findBookingByUser_ShouldThrowNotFoundExceptionUser() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            bookingService.findBookingItemByUserId(user.getId(), "ALL");
        });
        assertEquals("Пользователь с id = 1 не найден", exception.getMessage());
        verify(bookingRepository, never()).findByBookerIdOrderByStartDesc(anyLong());
    }

    @Test
    void findBookingById_shouldReturnBookingDto() {
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        BookingDto result = bookingService.findBookingById(user.getId(), booking.getId());
        assertNotNull(result);
        assertEquals(booking.getId(), result.getId());
        assertEquals(booking.getItem().getId(), result.getItem().getId());
        verify(bookingRepository).findById(booking.getId());
    }

    @Test
    void findBookingById_shouldValidationException() {
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        ValidationException validationException = assertThrows(ValidationException.class, () -> {
            bookingService.findBookingById(3L, booking.getId());
        });
        assertEquals("Пользователь с id 3 не является владельцем вещи или автором бронирования2", validationException.getMessage());
        verify(bookingRepository).findById(booking.getId());
    }

    @Test
    void findBookingByOwnerIdAll_shouldIsReturnBookingDto() {
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(bookingRepository.findByOwnerIdOrderByStartDesc(owner.getId())).thenReturn(Collections.singletonList(booking));
        List<BookingDto> result = bookingService.findBookingItemByOwnerId(owner.getId(), "ALL");
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(booking.getId(), result.get(0).getId());
        verify(userRepository).findById(owner.getId());
        verify(bookingRepository).findByOwnerIdOrderByStartDesc(owner.getId());
    }

    @Test
    void findBookingByOwnerIdCurrent_shouldIsReturnBookingDto() {
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(bookingRepository.findByOwnerIdAndStatusCurrent(anyLong(), any())).thenReturn(Collections.singletonList(booking));
        List<BookingDto> result = bookingService.findBookingItemByOwnerId(owner.getId(), "CURRENT");
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(booking.getId(), result.get(0).getId());
        verify(userRepository).findById(owner.getId());
        verify(bookingRepository).findByOwnerIdAndStatusCurrent(anyLong(), any());
    }

    @Test
    void confirmationBooking_isTrue() {
        booking.setId(1L);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        BookingDto result = bookingService.confirmationBooking(owner.getId(), booking.getId(), true);
        assertEquals(Status.APPROVED, result.getStatus());
        verify(bookingRepository).findById(booking.getId());
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void confirmationBooking_isFalse() {
        booking.setId(1L);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        BookingDto result = bookingService.confirmationBooking(owner.getId(), booking.getId(), false);
        assertEquals(Status.REJECTED, result.getStatus());
        verify(bookingRepository).findById(booking.getId());
        verify(bookingRepository).save(any(Booking.class));
    }
}
