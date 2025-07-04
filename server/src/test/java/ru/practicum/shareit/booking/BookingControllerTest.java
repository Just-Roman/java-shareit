package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
class BookingControllerTest {
    @MockBean
    private BookingService bookingService;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    void setBookingItem() throws Exception {
        BookingCreateDto createDto = BookingCreateDto.builder()
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .itemId(1L)
                .build();
        BookingDto responseDto = BookingDto.builder()
                .id(1L)
                .start(createDto.getStart())
                .end(createDto.getEnd())
                .item(ItemDto.builder().id(1L).name("Drill").description("Power drill").available(true).build())
                .booker(UserDto.builder().id(1L).name("John").email("john@example.com").build())
                .status(BookingStatus.WAITING)
                .build();
        Mockito.when(bookingService.setBookingItem(any(BookingCreateDto.class), eq(1L))).thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("WAITING"));

        Mockito.verify(bookingService, Mockito.times(1)).setBookingItem(any(BookingCreateDto.class), eq(1L));
    }

    @Test
    void setBookingStatus() throws Exception {
        BookingDto responseDto = BookingDto.builder()
                .id(2L)
                .status(BookingStatus.APPROVED)
                .build();
        Mockito.when(bookingService.setBookingStatus(eq(1L), eq(2L), eq(true))).thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.patch("/bookings/2")
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.status").value("APPROVED"));

        Mockito.verify(bookingService, Mockito.times(1)).setBookingStatus(eq(1L), eq(2L), eq(true));
    }

    @Test
    void getBookingById() throws Exception {
        BookingDto responseDto = BookingDto.builder()
                .id(3L)
                .status(BookingStatus.REJECTED)
                .build();
        Mockito.when(bookingService.getBookingById(eq(1L), eq(3L))).thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/bookings/3")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath(".id").value(3))
                .andExpect(jsonPath(".status").value("REJECTED"));

        Mockito.verify(bookingService, Mockito.times(1)).getBookingById(eq(1L), eq(3L));
    }

    @Test
    void getBookingByBookerIdWhereTime() throws Exception {
        List<BookingDto> bookings = List.of(
                BookingDto.builder().id(4L).status(BookingStatus.APPROVED).build()
        );
        Mockito.when(bookingService.getBookingByBookerIdWhereTime(eq(1L), eq("ALL"))).thenReturn(bookings);

        mockMvc.perform(MockMvcRequestBuilders.get("/bookings")
                        .param("state", "ALL")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(4L))
                .andExpect(jsonPath("$[0].status").value("APPROVED"));

        Mockito.verify(bookingService, Mockito.times(1)).getBookingByBookerIdWhereTime(eq(1L), eq("ALL"));
    }

    @Test
    void getBookingByOwnerIdWhereTime() throws Exception {
        List<BookingDto> bookings = List.of(
                BookingDto.builder().id(5L).status(BookingStatus.CANCELED).build()
        );
        Mockito.when(bookingService.getBookingByOwnerIdWhereTime(eq(1L), eq("ALL"))).thenReturn(bookings);

        mockMvc.perform(MockMvcRequestBuilders.get("/bookings/owner")
                        .param("state", "ALL")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(5L))
                .andExpect(jsonPath("$[0].status").value("CANCELED"));

        Mockito.verify(bookingService, Mockito.times(1)).getBookingByOwnerIdWhereTime(eq(1L), eq("ALL"));
    }
}