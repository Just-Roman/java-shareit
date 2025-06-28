package ru.practicum.shareit.booking.dto;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookingCreateDto {
	@NotNull
	private long itemId;
	@FutureOrPresent
	private LocalDateTime start;
	@Future
	private LocalDateTime end;

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
