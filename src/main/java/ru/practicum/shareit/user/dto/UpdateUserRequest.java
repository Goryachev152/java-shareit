package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class UpdateUserRequest {
    String name;
    @Email
    String email;
}
