package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import ru.practicum.shareit.request.model.ItemRequest;

/**
 * TODO Sprint add-controllers.
 */
@Value
@Builder(toBuilder = true)
public class Item {
    Long id;
    @NotBlank(message = "Имя не должно быть пустым")
    String name;
    @NotBlank(message = "Описание не должно быть пустым")
    String description;
    @NotNull(message = "Должен присутствовать статус")
    Boolean available;
    Long owner;
    ItemRequest request;
}
