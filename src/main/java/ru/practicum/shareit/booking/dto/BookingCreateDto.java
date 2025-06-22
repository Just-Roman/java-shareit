package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
@Data
public class BookingCreateDto {
    @NotNull
    @FutureOrPresent
    private LocalDateTime start;
    @NotNull
    @Future
    private LocalDateTime end;
    @NotNull
    private Long itemId;

    @AssertTrue(message = "Дата окончания должна быть позже даты начала")
    boolean isStartBeforeEnd() {
        return start != null &&
                end != null &&
                start.isBefore(end);
    }

    public void normalizeTimestamps() {
        if (start != null) {
            start = start.truncatedTo(ChronoUnit.SECONDS);
        }
        if (end != null) {
            end = end.truncatedTo(ChronoUnit.SECONDS);
        }
    }
}
