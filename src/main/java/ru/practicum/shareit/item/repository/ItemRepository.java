package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class ItemRepository {
    private final Map<Long, Item> itemMap = new HashMap<>();

    public Item createItem(Long userId, Item item) {
        item = item.toBuilder()
                .id(getNextId())
                .owner(userId)
                .build();
        itemMap.put(item.getId(), item);
        return item;
    }

    public Item updateItem(Long itemId, Item item) {
        itemMap.put(itemId, item);
        return item;
    }

    public List<Item> getAllItemByUserId(Long userId) {
        return itemMap.values()
                .stream()
                .filter(item -> item.getOwner().equals(userId))
                .toList();
    }

    public Optional<Item> getByIdItem(Long itemId) {
        return Optional.ofNullable(itemMap.get(itemId));
    }

    public List<Item> getSearch(String text) {
        return itemMap.values()
                .stream()
                .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase())
                        || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .filter(item -> item.getAvailable().equals(true))
                .toList();
    }

    private Long getNextId() {
        long currentMaxId = itemMap.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
