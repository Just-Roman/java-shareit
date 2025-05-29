package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item create(Item item);

    Item update(Item item);

    Item getItemById(long itemId);

    List<Item> getItemByOwnerId(long ownerId);

    List<Item> getItemsByNameOrDescription(String text);
}
