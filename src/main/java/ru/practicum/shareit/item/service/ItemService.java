package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoUser;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    ItemDto createItem(Long userId, Item item);

    ItemDto updateItem(Long userId, Long itemId, Item item);

    List<ItemDtoUser> getAllItemByUserId(Long userId);

    ItemDto getByIdItem(Long itemId);

    List<ItemDto> getSearch(String text);
}
