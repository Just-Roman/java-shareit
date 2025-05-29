package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Null;
import lombok.Data;
import ru.practicum.shareit.booking.Status;

import java.time.LocalDateTime;

@Data
public class BookingUpdateDto {
    @Null
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Long itemId;
    private Long booker;
    private Status status;
}
