package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRequestMapper itemRequestMapper;

    @Override
    public ItemRequestDto createItemRequest(long requestorId, ItemRequestCreateDto createDto) {
        User requestor = checkAndReturnUser(requestorId);
        ItemRequest itemRequest = itemRequestMapper.itemRequestCreateDtoToModel(createDto);
        itemRequest.setRequestor(requestor);
        itemRequest = itemRequestRepository.save(itemRequest);
        return itemRequestMapper.modelToItemRequestDto(itemRequest);
    }

    @Override
    public List<ItemRequestDto> getItemRequestsByUserId(long requestorId) {
        checkAndReturnUser(requestorId);
        List<ItemRequest> requests = itemRequestRepository.findAllByRequestorIdSorted(requestorId);
        if (requests.isEmpty()) {
            throw new NotFoundException("У пользователя с ID " + requestorId + " нет запросов");
        }
        return itemRequestMapper.listModelToItemRequestDto(requests);
    }

    @Override
    public List<ItemRequestDto> findAllByNotRequestorIdSorted(long requestorId) {
        checkAndReturnUser(requestorId);
        List<ItemRequest> requests = itemRequestRepository.findAllByNotRequestorIdSorted(requestorId);
        if (requests.isEmpty()) {
            throw new NotFoundException("У пользователя с ID " + requestorId + " нет запросов");
        }
        return itemRequestMapper.listModelToItemRequestDto(requests);
    }

    @Override
    public ItemRequestDto getItemRequestById(long itemRequestId) {
        ItemRequest itemRequest = itemRequestRepository.findById(itemRequestId)
                .orElseThrow(() -> new NotFoundException("ItemRequest не найден"));
        return itemRequestMapper.modelToItemRequestDto(itemRequest);
    }

    private User checkAndReturnUser(long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User не найден"));
    }

}
