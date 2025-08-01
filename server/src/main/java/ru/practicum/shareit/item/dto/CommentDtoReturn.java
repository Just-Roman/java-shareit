package ru.practicum.shareit.item.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class CommentDtoReturn {
    private Long id;
    private String text;
    private String authorName;
    private LocalDateTime created;
}
