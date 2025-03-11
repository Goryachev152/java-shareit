package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoRequest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOwner;
import ru.practicum.shareit.item.dto.ItemDtoUser;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    ItemDto createItem(Long userId, Item item);

    ItemDto updateItem(Long userId, Long itemId, Item item);

    List<ItemDtoOwner> getAllItemByUserId(Long userId);

    ItemDtoUser getByIdItem(Long itemId);

    List<ItemDto> getSearch(String text);

    CommentDto addComment(Long userId, Long itemId, CommentDtoRequest request);
}
