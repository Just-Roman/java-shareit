package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.user.UserMapper;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class BookingMapper {
    private final ItemMapper itemMapper;
    private final UserMapper userMapper;

    public Booking bookingCreateDtoToModel(BookingCreateDto dto) {
        return Booking.builder()
                .start(dto.getStart())
                .end(dto.getEnd())
                .status(BookingStatus.WAITING)
                .build();
    }

    public BookingDto modelToDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(itemMapper.modelToItemDto(booking.getItem()))
                .booker(userMapper.modelToDto(booking.getBooker()))
                .status(booking.getStatus())
                .build();
    }

    public List<BookingDto> listModelToDto(List<Booking> bookings) {
        List<BookingDto> bookingDto = new ArrayList<>();
        for (Booking booking : bookings) {
            bookingDto.add(modelToDto(booking));
        }
        return bookingDto;
    }


}
