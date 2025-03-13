package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class BookItemRequestDto {
    @NotNull
    Long itemId;
    @NotNull
    LocalDateTime start;
    @NotNull
    LocalDateTime end;
}
