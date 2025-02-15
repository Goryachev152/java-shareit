package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoUser;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto createItem(Long userId, Item item) {
        validNotFoundUser(userId);
        Item newItem = itemRepository.createItem(userId, item);
        log.info("Вещь с id {} добавлена в сервис", newItem.getId());
        return ItemMapper.mapToItemDto(newItem);
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, Item item) {
        validNotFoundUser(userId);
        Item updateItem = validNotFoundItem(itemId);
        if (!updateItem.getOwner().equals(userId)) {
            throw new ValidationException("Пользователь с id " + userId +
                    " не является владельцем вещи с id " + itemId);
        }
        if (item.getName() != null && item.getDescription() != null && item.getAvailable() != null) {
            updateItem = ItemMapper.mapToUpdateItemAllFields(updateItem, item);
        } else if (item.getName() != null && item.getDescription() != null) {
            updateItem = ItemMapper.mapToUpdateItemNameAndDescription(updateItem, item);
        } else if (item.getName() != null && item.getAvailable() != null) {
            updateItem = ItemMapper.mapToUpdateItemNameAndAvailable(updateItem, item);
        } else if (item.getAvailable() != null && item.getDescription() != null) {
            updateItem = ItemMapper.mapToUpdateItemAvailableAndDescription(updateItem, item);
        } else if (item.getName() != null) {
            updateItem = updateItem.toBuilder().name(item.getName()).build();
        } else if (item.getDescription() != null) {
            updateItem = updateItem.toBuilder().description(item.getDescription()).build();
        } else if (item.getAvailable() != null) {
            updateItem = updateItem.toBuilder().available(item.getAvailable()).build();
        }
        itemRepository.updateItem(itemId, updateItem);
        log.info("Вещь с id {} обновлена в сервисе", itemId);
        return ItemMapper.mapToItemDto(updateItem);
    }

    @Override
    public List<ItemDtoUser> getAllItemByUserId(Long userId) {
        validNotFoundUser(userId);
        List<Item> itemList = itemRepository.getAllItemByUserId(userId);
        return itemList.stream()
                .map(ItemMapper::mapToItemDtoUser)
                .toList();
    }

    @Override
    public ItemDto getByIdItem(Long itemId) {
        Item item = validNotFoundItem(itemId);
        return ItemMapper.mapToItemDto(item);
    }

    @Override
    public List<ItemDto> getSearch(String text) {
        List<Item> itemList = new ArrayList<>();
        if (!text.isBlank()) {
            itemList = itemRepository.getSearch(text);
        }
        return itemList.stream()
                .map(ItemMapper::mapToItemDto)
                .toList();
    }

    private void validNotFoundUser(Long userId) {
        Optional<User> user = userRepository.getByIdUser(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
    }

    private Item validNotFoundItem(Long itemId) {
        Optional<Item> item = itemRepository.getByIdItem(itemId);
        if (item.isPresent()) {
            return item.get();
        } else {
            throw new NotFoundException("Вещь с id " + itemId + " не найдена");
        }
    }
}
