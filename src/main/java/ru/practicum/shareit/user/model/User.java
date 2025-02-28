package ru.practicum.shareit.user.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * TODO Sprint add-controllers.
 */
@Value
@Builder(toBuilder = true)
@EqualsAndHashCode(of = "email")
public class User {
    Long id;
    @NotBlank(message = "Имя не может быть пустым")
    String name;
    @Email(message = "Email введен не корректно")
    @NotBlank(message = "Email не может быть пустым")
    String email;
}
