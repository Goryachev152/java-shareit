package ru.practicum.shareit.Item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.Item.dto.CommentRequestDto;
import ru.practicum.shareit.Item.dto.ItemRequestDto;
import ru.practicum.shareit.Item.dto.UpdateItemRequestDto;

@RestController
@RequestMapping(path = "/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody ItemRequestDto item) {
        return itemClient.createItem(userId, item);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @PathVariable Long itemId, @Valid @RequestBody CommentRequestDto commentRequestDto) {
        return itemClient.addComment(userId, itemId, commentRequestDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @PathVariable Long itemId,
                              @Valid @RequestBody UpdateItemRequestDto item) {
        return itemClient.updateItem(userId, itemId, item);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItemByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemClient.getAllItemByUserId(userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getByIdItem(@PathVariable Long itemId) {
        return itemClient.getByIdItem(itemId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getSearch(@RequestParam("text") String text) {
        return itemClient.getSearch(text);
    }
}
