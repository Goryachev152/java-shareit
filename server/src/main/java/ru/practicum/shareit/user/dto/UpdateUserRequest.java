package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class UpdateUserRequest {
    String name;
    String email;
}
