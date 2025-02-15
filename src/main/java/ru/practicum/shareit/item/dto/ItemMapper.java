package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

public class ItemMapper {

    public static Item mapToItem(ItemDto itemDto) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .owner(itemDto.getOwner())
                .request(itemDto.getRequest())
                .build();
    }

    public static ItemDto mapToItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(item.getOwner())
                .request(item.getRequest())
                .build();
    }

    public static ItemDtoUser mapToItemDtoUser(Item item) {
        return ItemDtoUser.builder()
                .name(item.getName())
                .description(item.getDescription())
                .build();
    }

    public static Item mapToUpdateItemAllFields(Item updateItem, Item item) {
        return updateItem.toBuilder()
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

    public static Item mapToUpdateItemNameAndDescription(Item updateItem, Item item) {
        return updateItem.toBuilder()
                .name(item.getName())
                .description(item.getDescription())
                .build();
    }

    public static Item mapToUpdateItemNameAndAvailable(Item updateItem, Item item) {
        return updateItem.toBuilder()
                .name(item.getName())
                .available(item.getAvailable())
                .build();
    }

    public static Item mapToUpdateItemAvailableAndDescription(Item updateItem, Item item) {
        return updateItem.toBuilder()
                .available(item.getAvailable())
                .description(item.getDescription())
                .build();
    }
}
