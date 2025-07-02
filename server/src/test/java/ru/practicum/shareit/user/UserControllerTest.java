package ru.practicum.shareit.user;

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
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
class UserControllerTest {
    @MockBean
    private UserService service;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void create() throws Exception {
        UserCreateDto createDto = UserCreateDto.builder().name("John").email("john@example.com").build();
        UserDto responseDto = UserDto.builder().id(1L).name("John").email("john@example.com").build();
        Mockito.when(service.create(any(UserCreateDto.class))).thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.email").value("john@example.com"));

        Mockito.verify(service, Mockito.times(1)).create(any(UserCreateDto.class));
    }

    @Test
    void update() throws Exception {
        UserUpdateDto updateDto = UserUpdateDto.builder().name("Jane").email("jane@example.com").build();
        UserDto responseDto = UserDto.builder().id(1L).name("Jane").email("jane@example.com").build();
        Mockito.when(service.update(eq(1L), any(UserUpdateDto.class))).thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Jane"))
                .andExpect(jsonPath("$.email").value("jane@example.com"));

        Mockito.verify(service, Mockito.times(1)).update(eq(1L), any(UserUpdateDto.class));
    }

    @Test
    void getUserById() throws Exception {
        UserDto responseDto = UserDto.builder().id(1L).name("John").email("john@example.com").build();
        Mockito.when(service.getUserById(1L)).thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.email").value("john@example.com"));

        Mockito.verify(service, Mockito.times(1)).getUserById(1L);
    }

    @Test
    void getAll() throws Exception {
        List<UserDto> users = List.of(
                UserDto.builder().id(1L).name("John").email("john@example.com").build(),
                UserDto.builder().id(2L).name("Jane").email("jane@example.com").build()
        );
        Mockito.when(service.getAll()).thenReturn(users);

        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("John"))
                .andExpect(jsonPath("$[0].email").value("john@example.com"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Jane"))
                .andExpect(jsonPath("$[1].email").value("jane@example.com"));

        Mockito.verify(service, Mockito.times(1)).getAll();
    }

    @Test
    void deleteUser() throws Exception {
        Mockito.doNothing().when(service).deleteUser(1L);
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/1"))
                .andExpect(status().isOk());

        Mockito.verify(service, Mockito.times(1)).deleteUser(1L);
    }
}