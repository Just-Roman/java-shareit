package ru.practicum.shareit.item.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
@Sql(scripts = "/data/data_repository.sql")
class ItemServiceTest {
    private final ItemService itemService;
    private final UserService userService;

    ItemCreateDto itemCreateDto = ItemCreateDto.builder()
            .name("Ящик")
            .description("Ящик деревянный")
            .available(true)
            .build();
    UserCreateDto userCreateDto = UserCreateDto.builder()
            .name("Samson")
            .email("user@mail.ru")
            .build();


    @Test
    void createComment() {
        CommentCreateDto createDto = new CommentCreateDto();
        createDto.setText("Супер!");

        CommentDtoReturn commentDtoReturn = itemService.createComment(2, 1, createDto);

        assertThat(commentDtoReturn.getText(), equalTo(createDto.getText()));
        assertThrows(BadRequestException.class, () -> itemService.createComment(99, 99, createDto));
    }

    @Test
    void getByItemIdWithComment() {
        ItemCommentDto itemCommentDto = itemService.getByItemIdWithComment(1, 1);
        assertThat(itemCommentDto.getId(), equalTo(1L));
        assertThat(itemCommentDto.getOwner(), notNullValue());
        assertThat(itemCommentDto.getComments().size(), equalTo(1));
    }

    @Test
    void allItemByOwnerIdWithComment() {
        List<ItemCommentDto> itemCommentDto = itemService.allItemByOwnerIdWithComment(1L);
        assertThat(itemCommentDto.size(), equalTo(1));
        assertThat(itemCommentDto.getFirst().getOwner().getId(), equalTo(1L));
        assertThat(itemCommentDto.getFirst().getComments().size(), equalTo(1));
    }

    @Test
    void createItem() {
        UserDto userCreatedDto = userService.create(userCreateDto);
        ItemDto itemCreatedDto = itemService.createItem(itemCreateDto, userCreatedDto.getId());

        assertThat(itemCreatedDto.getName(), equalTo(itemCreateDto.getName()));
        assertThat(itemCreatedDto.getDescription(), equalTo(itemCreateDto.getDescription()));
        assertThat(itemCreatedDto.getAvailable(), equalTo(itemCreateDto.getAvailable()));
        assertThat(itemCreatedDto.getOwner(), equalTo(userCreatedDto));
    }

    @Test
    void updateItem() {
        ItemUpdateDto updateDto = ItemUpdateDto.builder()
                .name("Велосипед")
                .description("Детский велосипед")
                .available(false)
                .build();


        UserDto userCreatedDto = userService.create(userCreateDto);
        ItemDto itemCreatedDto = itemService.createItem(itemCreateDto, userCreatedDto.getId());
        ItemDto itemUpdatedDto = itemService.updateItem(updateDto, userCreatedDto.getId(), itemCreatedDto.getId());

        assertThrows(ConflictException.class, () -> itemService.updateItem(updateDto, 1, 3));
        assertThat(updateDto.getName(), equalTo(itemUpdatedDto.getName()));
        assertThat(updateDto.getDescription(), equalTo(itemUpdatedDto.getDescription()));
        assertThat(updateDto.getAvailable(), equalTo(itemUpdatedDto.getAvailable()));
        assertThat(itemUpdatedDto.getOwner(), equalTo(userCreatedDto));
    }

    @Test
    void getItemsByNameOrDescription() {
        List<ItemDto> items = itemService.getItemsByNameOrDescription("Лопата");
        assertThat(items.size(), equalTo(1));
        assertThat(items.getFirst().getName(), equalTo("Лопата"));
    }
}