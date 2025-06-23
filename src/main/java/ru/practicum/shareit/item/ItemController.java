package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping("/{itemId}/comment")
    public CommentDtoReturn createComment(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable long itemId,
                                          @Validated @RequestBody CommentCreateDto createDto) {
        return itemService.createComment(userId, itemId, createDto);
    }

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @Validated @RequestBody ItemCreateDto createDto) {
        return itemService.createItem(createDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId,
                          @Validated @RequestBody ItemUpdateDto updateDto) {
        return itemService.update(updateDto, userId, itemId);
    }

    @GetMapping("/{itemId}")
    public ItemCommentDto getItemById(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId) {
        return itemService.getByItemIdWithComment(userId, itemId);
    }

    @GetMapping
    public List<ItemCommentDto> getItemByOwnerId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.findAllByOwnerIdWithBookings(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsByNameOrDescription(@RequestParam String text) {
        return itemService.getItemsByNameOrDescription(text);
    }

}
