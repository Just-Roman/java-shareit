package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.Map;

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

    public ResponseEntity<Object> createComment(long userId, Long itemId, CommentCreateDto createDto) {
        return post("/" + itemId + "/comment", userId, createDto);
    }

    public ResponseEntity<Object> createItem(Long userId, ItemCreateDto createDto) {
        return post("", userId, createDto);
    }

    public ResponseEntity<Object> updateItem(Long userId, Long itemId, ItemUpdateDto updateDto) {
        return patch("/" + itemId, userId, updateDto);
    }

    public ResponseEntity<Object> getByItemIdWithComment(Long userId, Long itemId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> allItemByOwnerIdWithComment(Long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> getItemsByNameOrDescription(Long userId, String text) {
        Map<String, Object> parameters = Map.of("text", text);
        return get("/search?text={text}", userId, parameters);
    }


}
