package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingMapper bookingMapper;
    private final BookingRepository bookingRepository;

    @Override
    public BookingDto setBookingItem(BookingCreateDto createDto, long bookerId) {
        User user = checkAndReturnUser(bookerId);
        Item item = checkAndReturnItem(createDto.getItemId());

        if (!item.getAvailable()) {
            throw new BadRequestException("Вещь недоступна для бронирования");
        }
        if (bookingRepository.existsByItemIdAndTimeRange(
                createDto.getItemId(), createDto.getStart(), createDto.getStart())) {
            throw new ConflictException("Вещь уже забронирована на указанные даты");
        }
        Booking booking = bookingMapper.bookingCreateDtoToModel(createDto);
        booking.setItem(item);
        booking.setBooker(user);
        booking = bookingRepository.save(booking);
        return bookingMapper.modelToDto(booking);
    }

    @Override
    public BookingDto setBookingStatus(long userId, long bookingId, boolean approve) {
        Booking booking = checkAndReturnBooking(bookingId);

        if (booking.getItem().getOwner().getId() != userId) {
            throw new BadRequestException("Изменить статус бронирования может только владелец вещи");
        }
        if (approve) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        bookingRepository.save(booking);
        return bookingMapper.modelToDto(booking);
    }

    @Override
    public BookingDto getBookingById(long userId, long bookingId) {
        Booking booking = checkAndReturnBooking(bookingId);
        long owner = booking.getItem().getOwner().getId();
        long broker = booking.getBooker().getId();

        if (userId != owner && userId != broker) {
            throw new ConflictException("Данные о бронировании доступны только автору или владельцу вещи");
        }
        return bookingMapper.modelToDto(booking);
    }

    @Override
    public List<BookingDto> getBookingByBookerIdWhereTime(long booker, String state) {
        checkAndReturnUser(booker);
        List<Booking> bookings = bookingRepository.getBookingByBookerIdWhereTime(booker, state);
        if (bookings.isEmpty()) {
            throw new ConflictException("Данные о бронировании не найдены");
        }
        return bookingMapper.listModelToDto(bookings);
    }

    @Override
    public List<BookingDto> getBookingByOwnerIdWhereTime(long ownerId, String state) {
        checkAndReturnUser(ownerId);
        List<Booking> bookings = bookingRepository.getBookingByOwnerIdWhereTime(ownerId, state);
        if (bookings.isEmpty()) {
            throw new ConflictException("Данные о бронировании не найдены");
        }
        return bookingMapper.listModelToDto(bookings);
    }

    private Booking checkAndReturnBooking(long bookingId) {
        return bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("Booking не найден"));
    }

    private Item checkAndReturnItem(long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Item не найден"));
    }

    private User checkAndReturnUser(long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User не найден"));
    }

}
