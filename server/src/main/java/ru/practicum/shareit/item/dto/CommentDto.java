package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class CommentDto {
    private Long id;
    private String text;
    private ItemDto item;
    private UserDto author;
    private LocalDateTime created;

    public CommentDto(Long id, String text, ItemDto item, UserDto author, LocalDateTime created) {
        this.id = id;
        this.text = text;
        this.item = item;
        this.author = author;
        this.created = created;
    }

}
