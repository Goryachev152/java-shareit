package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class ItemDtoRequest {
    String name;
    String description;
    Boolean available;
    Long requestId;
}
