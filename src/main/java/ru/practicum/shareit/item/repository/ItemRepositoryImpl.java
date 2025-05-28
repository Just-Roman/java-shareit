package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ItemRepositoryImpl implements ItemRepository {
    private long id = 1L;
    private final Map<Long, Item> itemMap = new HashMap<>();

    @Override
    public long getNextId() {
        return id++;
    }

    @Override
    public Item create(Item item) {
        long id = getNextId();
        item.setId(id);
        itemMap.put(id, item);
        return item;
    }

    @Override
    public Item update(Item item) {
        long id = item.getId();
        checkItemId(id);
        checkOwnerId(item);
        Item itemSaved = itemMap.get(id);
        if (item.getName() != null) {
            itemSaved.setName(item.getName());
        }
        if (item.getDescription() != null) {
            itemSaved.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            itemSaved.setAvailable(item.getAvailable());
        }
        itemMap.put(id, itemSaved);
        return itemSaved;
    }

    @Override
    public Item getItemById(long itemId) {
        checkItemId(itemId);
        return itemMap.get(itemId);
    }

    @Override
    public List<Item> getItemByOwnerId(long ownerId) {
        return itemMap.values()
                .stream()
                .filter(item -> item.getOwner() == ownerId)
                .toList();
    }

    @Override
    public List<Item> getItemsByNameOrDescription(String text) {
        return itemMap.values().stream()
                .filter(item -> item.getAvailable().equals(true))
                .filter(item -> item.getName().toLowerCase().contains(text) ||
                        item.getDescription().toLowerCase().contains(text))
                .toList();
    }

    private void checkItemId(long id) {
        if (!itemMap.containsKey(id)) {
            throw new NotFoundException("item под номером: id = " + id + " отсутствует");
        }
    }

    private void checkOwnerId(Item item) {
        Item itemSaved = itemMap.get(item.getId());
        if (item.getOwner() != itemSaved.getOwner()) {
            throw new NotFoundException("Редактировать item может только владелец");
        }
    }

}
