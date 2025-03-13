package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoRequest;

import java.util.List;

public interface RequestService {

    ItemRequestDto createRequest(Long userId, ItemRequestDtoRequest request);

    List<ItemRequestDto> getRequestsByRequestorId(Long userId);

    List<ItemRequestDto> getAllRequests();

    ItemRequestDto getByRequestId(Long requestId);
}
