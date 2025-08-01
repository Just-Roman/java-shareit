package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDtoReturn;
import ru.practicum.shareit.item.model.Comment;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class CommentMapper {

    public Comment commentCreateDtoToModel(CommentCreateDto createDto) {
        return Comment.builder()
                .text(createDto.getText())
                .created(LocalDateTime.now())
                .build();
    }

    public CommentDtoReturn modelToReturnDto(Comment comment) {
        return CommentDtoReturn.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }
}
