package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.CommentMapper;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    public CommentDtoReturn createComment(long authorId, long itemId, CommentCreateDto createDto) {
        if (!bookingRepository.hasUserBookedItem(authorId, itemId)) {
            throw new BadRequestException("Добавлять комментарий может только тот, кто брал вещь в аренду");
        }
        User author = checkAndReturnUser(authorId);
        Item item = checkAndReturnItem(itemId);
        Comment comment = commentMapper.commentCreateDtoToModel(createDto);
        comment.setAuthor(author);
        comment.setItem(item);
        comment = commentRepository.save(comment);
        return commentMapper.modelToReturnDto(comment);
    }

    @Override
    public ItemCommentDto getByItemIdWithComment(long userId, long itemId) {
        ItemCommentDto itemCommentDto = itemRepository.findItemWithBookings(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));
        if (itemCommentDto.getOwner().getId() != userId) {
            itemCommentDto.setLastBooking(null);
            itemCommentDto.setNextBooking(null);
        }
        List<CommentDto> commentDto = commentRepository.findByItemId(itemId);
        itemCommentDto.setComments(commentDto);
        return itemCommentDto;
    }

    @Override
    public List<ItemCommentDto> allItemByOwnerIdWithComment(Long ownerId) {
        List<ItemCommentDto> items = itemRepository.allItemByOwnerIdWithComment(ownerId);

        if (items.isEmpty()) {
            throw new NotFoundException("У пользователя с ID " + ownerId + " нет вещей");
        }
        List<Long> itemIds = items.stream()
                .map(ItemCommentDto::getId)
                .collect(Collectors.toList());

        Map<Long, List<CommentDto>> commentsMap = commentRepository.findByItemIds(itemIds)
                .stream()
                .collect(Collectors.groupingBy(
                        comment -> comment.getItem().getId(),
                        Collectors.toList()
                ));
        items.forEach(item ->
                item.setComments(commentsMap.getOrDefault(item.getId(), Collections.emptyList()))
        );
        return items;
    }

    @Override
    public ItemDto createItem(ItemCreateDto createDto, long ownerId) {
        Item item = itemMapper.createDtoToModel(createDto);
        User user = checkAndReturnUser(ownerId);
        item.setOwner(user);
        if (createDto.getRequestId() != null) {
            ItemRequest itemRequest = itemRequestRepository.findById(createDto.getRequestId())
                    .orElseThrow(() -> new NotFoundException("ItemRequest не найден"));
            item.setRequest(itemRequest);
        }
        return itemMapper.modelToItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto updateItem(ItemUpdateDto updateDto, long ownerId, long itemId) {
        User owner = checkAndReturnUser(ownerId);
        Item item = checkAndReturnItem(itemId);

        if (item.getOwner().getId() != ownerId) {
            throw new ConflictException("Редактировать Item может только владелец");
        }
        if (updateDto.getName() != null) {
            item.setName(updateDto.getName());
        }
        if (updateDto.getDescription() != null) {
            item.setDescription(updateDto.getDescription());
        }
        if (updateDto.getAvailable() != null) {
            item.setAvailable(updateDto.getAvailable());
        }
        item.setOwner(owner);
        itemRepository.save(item);
        return itemMapper.modelToItemDto(item);
    }

    @Override
    public List<ItemDto> getItemsByNameOrDescription(String text) {
        if (text == null || text.isEmpty()) {
            return List.of();
        }
        List<Item> items = itemRepository.getItemsByNameOrDescription(text.toLowerCase());
        return itemMapper.listModelToDto(items);
    }

    private Item checkAndReturnItem(long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Item не найден"));
    }

    private User checkAndReturnUser(long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User не найден"));
    }

}
