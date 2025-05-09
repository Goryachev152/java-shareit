package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.shareit.item.dto.ItemDtoRequestResponse;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */

@Value
@Builder(toBuilder = true)
public class ItemRequestDto {
    Long id;
    String description;
    User requestor;
    LocalDateTime created;
    List<ItemDtoRequestResponse> items;
}
