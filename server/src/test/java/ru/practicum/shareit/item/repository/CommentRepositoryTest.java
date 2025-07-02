package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.item.dto.CommentDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {"/data/cleanup.sql", "/data/data.sql"})
class CommentRepositoryTest { // Данные для теста класса подготовлены в файле data.sql

    @Autowired
    private CommentRepository commentRepository;

    @Test
    void findByItemId() {
        CommentDto commentDto1 = CommentDto.builder()
                .id(1L)
                .text("Отличная дрель, спасибо!")
                .created(LocalDateTime.parse("2025-06-13T11:20:00"))
                .build();
        CommentDto commentDto2 = CommentDto.builder()
                .id(5L)
                .text("Дрель норм")
                .created(LocalDateTime.parse("2025-07-13T10:15:00"))
                .build();

        List<CommentDto> list = commentRepository.findByItemId(1L); // Отсортирован по дате создания: от нового к старому
        CommentDto listComment1 = list.getFirst();
        CommentDto listComment2 = list.getLast();

        assertThat(list.size(), equalTo(2));
        assertThat(listComment1.getId(), equalTo(commentDto2.getId()));
        assertThat(listComment1.getText(), equalTo(commentDto2.getText()));
        assertThat(listComment1.getCreated(), equalTo(commentDto2.getCreated()));
        assertThat(listComment1.getItem(), notNullValue());
        assertThat(listComment1.getItem().getOwner(), notNullValue());
        assertThat(listComment1.getAuthor(), notNullValue());

        assertThat(listComment2.getId(), equalTo(commentDto1.getId()));
        assertThat(listComment2.getText(), equalTo(commentDto1.getText()));
        assertThat(listComment2.getCreated(), equalTo(commentDto1.getCreated()));
        assertThat(listComment2.getItem(), notNullValue());
        assertThat(listComment2.getItem().getOwner(), notNullValue());
        assertThat(listComment2.getAuthor(), notNullValue());
    }

    @Test
    void findByItemIds() {
        CommentDto commentDto1 = CommentDto.builder()
                .id(3L)
                .text("Палатка с небольшим дефектом")
                .created(LocalDateTime.parse("2025-07-11T10:15:00"))
                .build();
        CommentDto commentDto2 = CommentDto.builder()
                .id(4L)
                .text("Рогатка как рогатка")
                .created(LocalDateTime.parse("2025-07-12T10:15:00"))
                .build();

        List<CommentDto> list = commentRepository.findByItemIds(List.of(3L, 4L)); // Отсортирован по дате создания: от нового к старому
        CommentDto listComment1 = list.getFirst();
        CommentDto listComment2 = list.getLast();

        assertThat(list.size(), equalTo(2));
        assertThat(listComment1.getId(), equalTo(commentDto2.getId()));
        assertThat(listComment1.getText(), equalTo(commentDto2.getText()));
        assertThat(listComment1.getCreated(), equalTo(commentDto2.getCreated()));
        assertThat(listComment1.getItem(), notNullValue());
        assertThat(listComment1.getItem().getOwner(), notNullValue());
        assertThat(listComment1.getAuthor(), notNullValue());

        assertThat(listComment2.getId(), equalTo(commentDto1.getId()));
        assertThat(listComment2.getText(), equalTo(commentDto1.getText()));
        assertThat(listComment2.getCreated(), equalTo(commentDto1.getCreated()));
        assertThat(listComment2.getItem(), notNullValue());
        assertThat(listComment2.getItem().getOwner(), notNullValue());
        assertThat(listComment2.getAuthor(), notNullValue());
    }

}