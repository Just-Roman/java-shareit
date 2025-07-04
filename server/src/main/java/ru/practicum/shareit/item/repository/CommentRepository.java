package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;

import java.util.Collection;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("""
            SELECT new ru.practicum.shareit.item.dto.CommentDto(
                c.id,
                c.text,
                new ru.practicum.shareit.item.dto.ItemDto(
                    i.id,
                    i.name,
                    i.description,
                    i.available,
                    new ru.practicum.shareit.user.dto.UserDto(
                        i.owner.id,
                        i.owner.name,
                        i.owner.email
                    )
                ),
                new ru.practicum.shareit.user.dto.UserDto(
                    u.id,
                    u.name,
                    u.email
                ),
                c.created
            )
            FROM Comment c
            JOIN c.item i
            JOIN c.author u
            WHERE c.item.id = :itemId
            ORDER BY c.created DESC""")
    List<CommentDto> findByItemId(@Param("itemId") Long itemId);

    @Query("""
            SELECT new ru.practicum.shareit.item.dto.CommentDto(
                c.id,
                c.text,
                new ru.practicum.shareit.item.dto.ItemDto(
                    i.id,
                    i.name,
                    i.description,
                    i.available,
                    new ru.practicum.shareit.user.dto.UserDto(
                        i.owner.id,
                        i.owner.name,
                        i.owner.email
                    )
                ),
                new ru.practicum.shareit.user.dto.UserDto(
                    u.id,
                    u.name,
                    u.email
                ),
                c.created
            )
            FROM Comment c
            JOIN c.item i
            JOIN c.author u
            WHERE c.item.id IN :itemIds
            ORDER BY c.created DESC""")
    List<CommentDto> findByItemIds(@Param("itemIds") Collection<Long> itemIds);

}
