package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Null;
import lombok.Data;
import ru.practicum.shareit.booking.Status;

import java.time.LocalDateTime;

@Data
public class BookingCreateDto {
    private Long id;
    @Null
    private LocalDateTime start;
    @Null
    private LocalDateTime end;
    @Null
    private Long itemId;
    @Null
    private Long booker;
    private Status status;
}
