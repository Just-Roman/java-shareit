package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final ItemMapper itemMapper;

    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") Long userId,
                          @Validated @RequestBody ItemCreateDto createDto) {
        Item item = itemService.create(itemMapper.createDtoToModel(createDto, userId));
        return itemMapper.modelToItemDto(item);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId,
                          @Validated @RequestBody ItemUpdateDto updateDto) {
        Item item = itemService.update(itemMapper.updateDtoToModel(updateDto, userId, itemId));
        return itemMapper.modelToItemDto(item);
    }


    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable Long itemId) {
        return itemMapper.modelToItemDto(itemService.getItemById(itemId));
    }

    @GetMapping
    public List<ItemDto> getItemByOwnerId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemMapper.listModelToDto(itemService.getItemByOwnerId(userId));
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsByNameOrDescription(@RequestParam String text) {
        return itemMapper.listModelToDto(itemService.getItemsByNameOrDescription(text));
    }

}
