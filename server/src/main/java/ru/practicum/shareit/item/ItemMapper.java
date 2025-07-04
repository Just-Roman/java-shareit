package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class ItemMapper {
    private final UserMapper userMapper;

    public Item createDtoToModel(ItemCreateDto dto) {
        return Item.builder()
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
                .owner(userMapper.modelToDto(item.getOwner()))
                .request(item.getRequest() != null ? item.getRequest().getId() : null)
                .build();
    }

    public List<ItemDto> listModelToDto(List<Item> items) {
        List<ItemDto> list = new ArrayList<>();
        if (items != null) {
            for (Item item : items) {
                list.add(modelToItemDto(item));
            }
        }
        return list;
    }

}
