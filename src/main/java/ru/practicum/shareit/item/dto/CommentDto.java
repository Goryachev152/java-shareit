package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;

@Value
@Builder(toBuilder = true)
public class CommentDto {
    Long id;
    String text;
    Item item;
    String authorName;
    LocalDateTime created;
}
