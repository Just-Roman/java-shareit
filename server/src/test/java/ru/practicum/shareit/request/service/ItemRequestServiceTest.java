package ru.practicum.shareit.request.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
@Sql(scripts = "/data/data_repository.sql")
class ItemRequestServiceTest {
    private final ItemRequestService itemRequestService;
    private final UserService service;

    ItemRequestCreateDto createDto = ItemRequestCreateDto.builder()
            .description("Нужен ноутбук")
            .build();

    @Test
    void createItemRequest() {
        ItemRequestDto createdRequestDto = itemRequestService.createItemRequest(1, createDto);
        assertThat(createDto.getDescription(), equalTo(createdRequestDto.getDescription()));
        assertThat(createdRequestDto.getRequestor(), notNullValue());
        assertThat(createdRequestDto.getItems().size(), equalTo(0));
        assertThrows(NotFoundException.class, () ->
                itemRequestService.createItemRequest(999, createDto));
    }

    @Test
    void getItemRequestsByUserId() {
        long requestorId = 1L;
        ItemRequestCreateDto createDto = ItemRequestCreateDto.builder()
                .description("Нужен ноутбук")
                .build();
        UserCreateDto usercreateDto = UserCreateDto.builder()
                .name("Samson")
                .email("user@mail.ru")
                .build();

        UserDto userDtoCreated = service.create(usercreateDto);
        ItemRequestDto createdRequestDto = itemRequestService.createItemRequest(2, createDto);
        List<ItemRequestDto> list = itemRequestService.getItemRequestsByUserId(requestorId);

        assertThat(list.size(), equalTo(1));
        assertThat(list.getFirst().getRequestor().getId(), equalTo(requestorId));
        assertThat(list.getFirst().getItems().getFirst(), notNullValue());
        assertThrows(NotFoundException.class, () ->
                itemRequestService.getItemRequestsByUserId(999));
        assertThrows(NotFoundException.class, () ->
                itemRequestService.getItemRequestsByUserId(createdRequestDto.getId()));
        assertThrows(NotFoundException.class, () ->
                itemRequestService.getItemRequestsByUserId(userDtoCreated.getId()));
    }

    @Test
    void findAllByNotRequestorIdSorted() {
        long requestorId = 1L;
        List<ItemRequestDto> list = itemRequestService.findAllByNotRequestorIdSorted(requestorId);

        assertThat(list.size(), equalTo(4));
        assertThrows(NotFoundException.class, () ->
                itemRequestService.findAllByNotRequestorIdSorted(999));
        for (ItemRequestDto dto : list) {
            assertThat(dto.getRequestor().getId(), not(equalTo(requestorId)));
            assertThat(dto.getItems().getFirst(), notNullValue());
        }

        ItemRequestDto createdRequestDto = itemRequestService.createItemRequest(2, createDto);
        assertThrows(NotFoundException.class, () ->
                itemRequestService.findAllByNotRequestorIdSorted(createdRequestDto.getId()));
    }

    @Test
    void getItemRequestById() {
        ItemRequestDto createdRequestDto = itemRequestService.createItemRequest(2, createDto);
        ItemRequestDto findRequestDto = itemRequestService.getItemRequestById(createdRequestDto.getId());

        assertThat(createDto.getDescription(), equalTo(findRequestDto.getDescription()));
        assertThat(createdRequestDto.getRequestor(), equalTo(findRequestDto.getRequestor()));
        assertThat(createdRequestDto.getItems().size(), equalTo(findRequestDto.getItems().size()));
        assertThrows(NotFoundException.class, () ->
                itemRequestService.getItemRequestById(999));
    }
}