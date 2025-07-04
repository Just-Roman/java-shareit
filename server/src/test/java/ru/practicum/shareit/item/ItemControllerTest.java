package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
class ItemControllerTest {
    @MockBean
    private ItemService itemService;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void createComment() throws Exception {
        CommentCreateDto createDto = CommentCreateDto.builder().text("Nice item").build();
        CommentDtoReturn responseDto = CommentDtoReturn.builder()
                .id(1L)
                .text("Nice item")
                .authorName("John")
                .created(LocalDateTime.now())
                .build();
        Mockito.when(itemService.createComment(eq(1L), eq(2L), any(CommentCreateDto.class))).thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/items/2/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.text").value("Nice item"));

        Mockito.verify(itemService, Mockito.times(1)).createComment(eq(1L), eq(2L), any(CommentCreateDto.class));
    }

    @Test
    void createItem() throws Exception {
        ItemCreateDto createDto = ItemCreateDto.builder().name("Drill").description("Powerful drill").available(true).build();
        ItemDto responseDto = ItemDto.builder()
                .id(1L)
                .name("Drill")
                .description("Powerful drill")
                .available(true)
                .owner(UserDto.builder().id(1L).name("John").email("john@example.com").build())
                .build();
        Mockito.when(itemService.createItem(any(ItemCreateDto.class), eq(1L))).thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Drill"));

        Mockito.verify(itemService, Mockito.times(1)).createItem(any(ItemCreateDto.class), eq(1L));
    }

    @Test
    void updateItem() throws Exception {
        ItemUpdateDto updateDto = ItemUpdateDto.builder().name("Updated Drill").description("Updated desc").available(false).build();
        ItemDto responseDto = ItemDto.builder()
                .id(1L)
                .name("Updated Drill")
                .description("Updated desc")
                .available(false)
                .owner(UserDto.builder().id(1L).name("John").email("john@example.com").build())
                .build();
        Mockito.when(itemService.updateItem(any(ItemUpdateDto.class), eq(1L), eq(1L))).thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.patch("/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Updated Drill"));

        Mockito.verify(itemService, Mockito.times(1)).updateItem(any(ItemUpdateDto.class), eq(1L), eq(1L));
    }

    @Test
    void getByItemIdWithComment() throws Exception {
        ItemCommentDto responseDto = ItemCommentDto.builder()
                .id(1L)
                .name("Drill")
                .description("Powerful drill")
                .available(true)
                .owner(UserDto.builder().id(1L).name("John").email("john@example.com").build())
                .comments(Collections.emptyList())
                .build();
        Mockito.when(itemService.getByItemIdWithComment(1L, 1L)).thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/items/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Drill"));

        Mockito.verify(itemService, Mockito.times(1)).getByItemIdWithComment(1L, 1L);
    }

    @Test
    void allItemByOwnerIdWithComment() throws Exception {
        List<ItemCommentDto> items = List.of(
                ItemCommentDto.builder().id(1L).name("Drill").description("Powerful drill").available(true).owner(null).comments(Collections.emptyList()).build()
        );
        Mockito.when(itemService.allItemByOwnerIdWithComment(1L)).thenReturn(items);

        mockMvc.perform(MockMvcRequestBuilders.get("/items")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Drill"));

        Mockito.verify(itemService, Mockito.times(1)).allItemByOwnerIdWithComment(1L);
    }

    @Test
    void getItemsByNameOrDescription() throws Exception {
        List<ItemDto> items = List.of(
                ItemDto.builder().id(2L).name("Hammer").description("Heavy hammer").available(true).owner(null).build()
        );
        Mockito.when(itemService.getItemsByNameOrDescription("hammer")).thenReturn(items);

        mockMvc.perform(MockMvcRequestBuilders.get("/items/search")
                        .header("X-Sharer-User-Id", 1L)
                        .param("text", "hammer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2L))
                .andExpect(jsonPath("$[0].name").value("Hammer"));

        Mockito.verify(itemService, Mockito.times(1)).getItemsByNameOrDescription("hammer");
    }
}