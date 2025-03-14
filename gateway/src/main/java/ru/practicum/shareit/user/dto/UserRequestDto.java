package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserRequestDto {
    @NotBlank(message = "Имя не может быть пустым")
    String name;
    @Email(message = "Email введен не корректно")
    @NotBlank(message = "Email не может быть пустым")
    String email;
}
