package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder(toBuilder = true)
public class BookingDtoRequest {
    Long itemId;
    LocalDateTime start;
    LocalDateTime end;
}
