package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto createItemRequest(long requestorId, ItemRequestCreateDto createDto);

    List<ItemRequestDto> getItemRequestsByUserId(long requestorId);

    List<ItemRequestDto> findAllByNotRequestorIdSorted(long requestorId);

    ItemRequestDto getItemRequestById(long itemRequestId);
}
