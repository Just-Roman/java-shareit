package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;

@Component
public class ItemMapper {

    public Item createDtoToModel(ItemCreateDto dto, long ownerId) {
        return Item.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .available(dto.getAvailable())
                .owner(ownerId)
                .build();
    }

    public Item updateDtoToModel(ItemUpdateDto dto, long ownerId, long itemId) {
        return Item.builder()
                .id(itemId)
                .name(dto.getName())
                .description(dto.getDescription())
                .available(dto.getAvailable())
                .owner(ownerId)
                .build();
    }

    public ItemDto modelToItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(item.getOwner())
                .build();
    }

    public List<ItemDto> listModelToDto(List<Item> items) {
        List<ItemDto> list = new ArrayList<>();
        for (Item item : items) {
            list.add(modelToItemDto(item));
        }
        return list;
    }

}
