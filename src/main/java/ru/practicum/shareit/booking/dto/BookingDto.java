package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Value
@Builder(toBuilder = true)
public class BookingDto {
    Long id;
    LocalDateTime start;
    LocalDateTime end;
    Long item;
    Long booker;
    Status status;
}
