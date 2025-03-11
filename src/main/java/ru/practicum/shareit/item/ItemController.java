package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoRequest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOwner;
import ru.practicum.shareit.item.dto.ItemDtoUser;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody Item item) {
        return itemService.createItem(userId, item);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @PathVariable Long itemId, @Valid @RequestBody CommentDtoRequest request) {
        return itemService.addComment(userId, itemId, request);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @PathVariable Long itemId,
                              @RequestBody Item item) {
        return itemService.updateItem(userId, itemId, item);
    }

    @GetMapping
    public List<ItemDtoOwner> getAllItemByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getAllItemByUserId(userId);
    }

    @GetMapping("/{itemId}")
    public ItemDtoUser getByIdItem(@PathVariable Long itemId) {
        return itemService.getByIdItem(itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> getSearch(@RequestParam("text") String text) {
        return itemService.getSearch(text);
    }
}
