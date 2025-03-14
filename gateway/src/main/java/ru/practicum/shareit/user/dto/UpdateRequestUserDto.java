package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UpdateRequestUserDto {
    String name;
    @Email
    String email;
}
