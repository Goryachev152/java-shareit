package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Value
@Builder(toBuilder = true)
public class ItemDtoUser {
    Long id;
    String name;
    String description;
    Boolean available;
    User owner;
    BookingDto lastBooking;
    BookingDto nextBooking;
    List<CommentDto> comments;
}
