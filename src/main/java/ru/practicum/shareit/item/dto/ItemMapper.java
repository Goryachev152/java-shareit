package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public class ItemMapper {

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

    public static ItemDtoOwner mapToItemDtoOwner(Item item, BookingDto last, BookingDto next, List<CommentDto> comments) {
        return ItemDtoOwner.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(item.getOwner())
                .lastBooking(last)
                .nextBooking(next)
                .comments(comments)
                .build();
    }

    public static ItemDtoUser mapToItemDtoUser(Item item, List<CommentDto> comments) {
        return ItemDtoUser.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(item.getOwner())
                .lastBooking(null)
                .nextBooking(null)
                .comments(comments)
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
