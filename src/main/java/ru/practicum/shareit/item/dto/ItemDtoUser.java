package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class ItemDtoUser {
    String name;
    String description;
}
