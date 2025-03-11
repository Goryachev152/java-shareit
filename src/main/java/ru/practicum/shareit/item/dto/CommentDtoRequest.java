package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class CommentDtoRequest {
    @NotBlank
    String text;
}
