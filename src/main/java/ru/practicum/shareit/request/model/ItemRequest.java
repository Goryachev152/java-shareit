package ru.practicum.shareit.request.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Value
@Builder(toBuilder = true)
public class ItemRequest {
    Long id;
    @NotBlank
    String description;
    @NotNull
    Long requestor;
    LocalDateTime created;
}
