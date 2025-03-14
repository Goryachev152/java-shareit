package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;

import java.util.List;

public interface BookingService {

    BookingDto addBooking(Long userId, BookingDtoRequest request);

    BookingDto confirmationBooking(Long userId, Long bookingId, boolean approved);

    BookingDto findBookingById(Long userId, Long bookingId);

    List<BookingDto> findBookingItemByUserId(Long bookerId, String state);

    List<BookingDto> findBookingItemByOwnerId(Long ownerId, String state);
}
