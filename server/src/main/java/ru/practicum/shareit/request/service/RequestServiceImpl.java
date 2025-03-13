package ru.practicum.shareit.request.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDtoRequestResponse;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoRequest;
import ru.practicum.shareit.request.dto.RequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Transactional
    @Override
    public ItemRequestDto createRequest(Long userId, ItemRequestDtoRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь " +
                "с id = " + userId + " не найден"));
        ItemRequest itemRequest = requestRepository.save(RequestMapper.mapToItemRequest(request, user));
        log.info("Заппрос с id {} добавлен в сервис", itemRequest.getId());
        return RequestMapper.mapToItemRequestDto(itemRequest);
    }

    @Override
    public List<ItemRequestDto> getRequestsByRequestorId(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь " +
                "с id = " + userId + " не найден"));
        List<ItemRequest> requestList = requestRepository.findAllByRequestorId(userId);
        List<Long> requestsId = requestList.stream().map(ItemRequest::getId).toList();
        List<Item> items = itemRepository.findAllByRequestId(requestsId);
        Map<Long, List<Item>> groupItem = items.stream().collect(Collectors.groupingBy(item -> item.getRequest().getId()));
        return requestList.stream()
                .map(request -> {
                    List<Item> itemList = groupItem.getOrDefault(request.getId(), Collections.emptyList());
                    List<ItemDtoRequestResponse> itemResponse = itemList.stream()
                            .map(ItemMapper::mapToItemResponse)
                            .toList();
                    return RequestMapper.mapToItemRequestDto(request, itemResponse);
                })
                .toList();
    }

    @Override
    public List<ItemRequestDto> getAllRequests() {
        List<ItemRequest> requestList = requestRepository.findAll();
        return requestList.stream().map(RequestMapper::mapToItemRequestDto).toList();
    }

    @Override
    public ItemRequestDto getByRequestId(Long requestId) {
        ItemRequest itemRequest = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос с id = " + requestId + " не найден"));
        List<Item> items = itemRepository.findByRequestId(requestId);
        List<ItemDtoRequestResponse> itemsDto = items.stream().map(ItemMapper::mapToItemResponse).toList();
        return RequestMapper.mapToItemRequestDto(itemRequest, itemsDto);
    }
}
