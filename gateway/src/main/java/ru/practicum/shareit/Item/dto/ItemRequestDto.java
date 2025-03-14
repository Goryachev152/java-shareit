package ru.practicum.shareit.Item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ItemRequestDto {
    @NotBlank
    String name;
    @NotNull
    String description;
    @NotNull
    Boolean available;
    Long requestId;
}
