package ru.practicum.shareit.Item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentRequestDto {
    @NotBlank
    String text;
}
