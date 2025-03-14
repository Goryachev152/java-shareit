package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Value
@Builder(toBuilder = true)
public class ItemRequestDto {
    Long id;
    String description;
    Long requestor;
    LocalDateTime created;
}
