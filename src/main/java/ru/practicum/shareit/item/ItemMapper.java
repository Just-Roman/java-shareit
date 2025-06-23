package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class ItemMapper {

    public Item createDtoToModel(ItemCreateDto dto) {
        return Item.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .available(dto.getAvailable())
                .build();
    }

    public Item updateDtoToModel(ItemUpdateDto dto, long itemId) {
        return Item.builder()
                .id(itemId)
                .name(dto.getName())
                .description(dto.getDescription())
                .available(dto.getAvailable())
                .build();
    }

    public ItemDto modelToItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(UserDto.builder()
                        .id(item.getOwner().getId())
                        .name(item.getOwner().getName())
                        .email(item.getOwner().getEmail())
                        .build())
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
