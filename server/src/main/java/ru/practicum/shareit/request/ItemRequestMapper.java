package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class ItemRequestMapper {
    private final UserMapper userMapper;
    private final ItemMapper itemMapper;

    public ItemRequest itemRequestCreateDtoToModel(ItemRequestCreateDto createDto) {
        return ItemRequest.builder()
                .description(createDto.getDescription())
                .created(LocalDateTime.now())
                .build();
    }

    public ItemRequestDto modelToItemRequestDto (ItemRequest itemRequest) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .requestor(userMapper.modelToDto(itemRequest.getRequestor()))
                .created(itemRequest.getCreated())
                .items(itemMapper.listModelToDto(itemRequest.getItems()))
                .build();
    }

    public List<ItemRequestDto> listModelToItemRequestDto(List<ItemRequest> requests) {
        List<ItemRequestDto> listDto = new ArrayList<>();
        for (ItemRequest request: requests) {
            listDto.add(modelToItemRequestDto(request));
        }
        return listDto;
    }

}
