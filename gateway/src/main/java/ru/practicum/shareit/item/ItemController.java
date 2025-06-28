package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @PathVariable Long itemId,
                                                @RequestBody @Valid CommentCreateDto createDto) {
        log.info("Creating Comment {}, userId={}, itemId={}", createDto, userId, itemId);
        return itemClient.createComment(userId, itemId, createDto);
    }

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @RequestBody @Valid ItemCreateDto createDto) {
        log.info("Creating Item {}, userId={}", createDto, userId);
        return itemClient.createItem(userId, createDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable Long itemId,
                                             @RequestBody @Valid ItemUpdateDto updateDto) {
        log.info("Patch updateItem {}, userId={}, itemId={}", updateDto, userId, itemId);
        return itemClient.updateItem(userId, itemId, updateDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getByItemIdWithComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                         @PathVariable Long itemId) {
        log.info("Get getByItemIdWithComment, userId={}, itemId={}", userId, itemId);
        return itemClient.getByItemIdWithComment(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> allItemByOwnerIdWithComment(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Get allItemByOwnerIdWithComment , userId={}", userId);
        return itemClient.allItemByOwnerIdWithComment(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getItemsByNameOrDescription(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                              @RequestParam String text) {
        log.info("Get getItemsByNameOrDescription, userId={}, text={}", userId, text);
        return itemClient.getItemsByNameOrDescription(userId, text);
    }

}
