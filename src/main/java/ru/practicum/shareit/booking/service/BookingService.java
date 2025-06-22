package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    BookingDto setBookingItem(BookingCreateDto createDto, long bookerId);

    BookingDto setBookingStatus(long userId, long bookingId, boolean approve);

    BookingDto getBookingById(long userId, long bookingId);

    List<BookingDto> getBookingByBookerIdWhereTime(long booker, String state);

    List<BookingDto> getBookingByOwnerIdWhereTime(long ownerId, String state);
}
