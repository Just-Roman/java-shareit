package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.dto.ItemCommentDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;


public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("""
            SELECT new ru.practicum.shareit.item.dto.ItemCommentDto(
                i.id,
                i.name,
                i.description,
                i.available,
                new ru.practicum.shareit.user.dto.UserDto(u.id, u.name, u.email),
                (SELECT MAX(b.start) FROM Booking b
                 WHERE b.item.id = i.id AND b.status = 'APPROVED' AND b.end < CURRENT_TIMESTAMP),
                (SELECT MIN(b.start) FROM Booking b
                 WHERE b.item.id = i.id AND b.status = 'APPROVED' AND b.start > CURRENT_TIMESTAMP)
            )
            FROM Item i
            JOIN User u ON i.owner.id = u.id
            WHERE i.id = :itemId""")
    Optional<ItemCommentDto> findItemWithBookings(@Param("itemId") Long itemId);

    @Query("""
            SELECT new ru.practicum.shareit.item.dto.ItemCommentDto(
                i.id,
                i.name,
                i.description,
                i.available,
                new ru.practicum.shareit.user.dto.UserDto(u.id, u.name, u.email),
                (SELECT MAX(b.start) FROM Booking b
                 WHERE b.item.id = i.id AND b.status = 'APPROVED' AND b.end < CURRENT_TIMESTAMP),
                (SELECT MIN(b.start) FROM Booking b
                 WHERE b.item.id = i.id AND b.status = 'APPROVED' AND b.start > CURRENT_TIMESTAMP)
            )
            FROM Item i
            JOIN User u ON i.owner.id = u.id
            WHERE i.owner.id = :ownerId
            ORDER BY i.id ASC""")
    List<ItemCommentDto> allItemByOwnerIdWithComment(@Param("ownerId") Long ownerId);

    @Query("SELECT i FROM Item i " +
            "WHERE i.available = true " +
            "AND (LOWER(i.name) LIKE LOWER(CONCAT('%', :text, '%')) " +
            "OR LOWER(i.description) LIKE LOWER(CONCAT('%', :text, '%')))")
    List<Item> getItemsByNameOrDescription(@Param("text") String text);
}
