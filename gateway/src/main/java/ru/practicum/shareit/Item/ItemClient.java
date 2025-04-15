package ru.practicum.shareit.Item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.Item.dto.CommentRequestDto;
import ru.practicum.shareit.Item.dto.ItemRequestDto;
import ru.practicum.shareit.Item.dto.UpdateItemRequestDto;
import ru.practicum.shareit.client.BaseClient;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> createItem(Long userId, ItemRequestDto itemRequestDto) {
        return post("", userId, itemRequestDto);
    }

    public ResponseEntity<Object> addComment(Long userId, Long itemId, CommentRequestDto commentRequestDto) {
        return post("/" + itemId + "/comment", userId, commentRequestDto);
    }

    public ResponseEntity<Object> updateItem(Long userId, Long itemId, UpdateItemRequestDto updateItemRequestDto) {
        return patch("/" + itemId, userId, updateItemRequestDto);
    }

    public ResponseEntity<Object> getAllItemByUserId(Long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> getByIdItem(Long itemId) {
        return get("/" + itemId);
    }

    public ResponseEntity<Object> getSearch(String text) {
        return get("/search?text=" + text);
    }
}
