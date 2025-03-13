package ru.practicum.shareit.Item.dto;

import lombok.Data;

@Data
public class UpdateItemRequestDto {
    String name;
    String description;
    Boolean available;
}
