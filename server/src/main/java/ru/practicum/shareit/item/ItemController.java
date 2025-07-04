package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping("/{itemId}/comment")
    public CommentDtoReturn createComment(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId,
                                          @RequestBody CommentCreateDto createDto) {
        return itemService.createComment(userId, itemId, createDto);
    }

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @RequestBody ItemCreateDto createDto) {
        return itemService.createItem(createDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId,
                              @RequestBody ItemUpdateDto updateDto) {
        return itemService.updateItem(updateDto, userId, itemId);
    }

    @GetMapping("/{itemId}")
    public ItemCommentDto getByItemIdWithComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @PathVariable Long itemId) {
        return itemService.getByItemIdWithComment(userId, itemId);
    }

    @GetMapping
    public List<ItemCommentDto> allItemByOwnerIdWithComment(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.allItemByOwnerIdWithComment(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsByNameOrDescription(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                     @RequestParam String text) {
        return itemService.getItemsByNameOrDescription(text);
    }

}
