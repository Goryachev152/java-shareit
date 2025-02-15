package ru.practicum.shareit.booking.model;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Value
@Builder(toBuilder = true)
public class Booking {
    Long id;
    LocalDateTime start;
    LocalDateTime end;
    @NotNull
    Long item;
    @NotNull
    Long booker;
    @NotNull
    Status status;
}
