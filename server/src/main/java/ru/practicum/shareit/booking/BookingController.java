package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto setBookingItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @RequestBody BookingCreateDto createDto) {
        return bookingService.setBookingItem(createDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto setBookingStatus(@RequestHeader("X-Sharer-User-Id") Long userId,
                                       @PathVariable Long bookingId, @RequestParam boolean approved) {
        return bookingService.setBookingStatus(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long bookingId) {
        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping
    public List<BookingDto> getBookingByBookerIdWhereTime(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                          @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getBookingByBookerIdWhereTime(userId, state);
    }

    @GetMapping("/owner")
    List<BookingDto> getBookingByOwnerIdWhereTime(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                  @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getBookingByOwnerIdWhereTime(userId, state);
    }

}
