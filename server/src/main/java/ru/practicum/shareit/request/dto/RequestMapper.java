package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.item.dto.ItemDtoRequestResponse;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class RequestMapper {

    public static ItemRequestDto mapToItemRequestDto(ItemRequest itemRequest) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .requestor(itemRequest.getRequestor())
                .created(itemRequest.getCreated())
                .build();
    }

    public static ItemRequestDto mapToItemRequestDto(ItemRequest itemRequest, List<ItemDtoRequestResponse> items) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .requestor(itemRequest.getRequestor())
                .created(itemRequest.getCreated())
                .items(items)
                .build();
    }

    public static ItemRequest mapToItemRequest(ItemRequestDtoRequest itemRequestDtoRequest, User user) {
        return ItemRequest.builder()
                .description(itemRequestDtoRequest.getDescription())
                .requestor(user)
                .created(LocalDateTime.now())
                .build();
    }
}
