package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Null;
import lombok.Data;
import ru.practicum.shareit.booking.BookingStatus;

import java.time.LocalDateTime;

@Data
public class BookingUpdateDto {
    @Null
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Long itemId;
    private Long booker;
    private BookingStatus bookingStatus;

    @AssertTrue(message = "Дата окончания должна быть позже даты начала")
    boolean isStartBeforeEnd() {
        return start != null &&
                end != null &&
                start.isBefore(end);
    }
}
