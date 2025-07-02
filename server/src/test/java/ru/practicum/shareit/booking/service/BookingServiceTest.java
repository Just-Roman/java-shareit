package ru.practicum.shareit.booking.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ConflictException;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
@Sql(scripts = "/data/data_repository.sql")
class BookingServiceTest {
    private static final Logger log = LoggerFactory.getLogger(BookingServiceTest.class);
    private final BookingService bookingService;

    BookingCreateDto createDto1 = BookingCreateDto.builder()
            .itemId(1L)
            .start(LocalDateTime.parse("2050-07-01T08:00:00"))
            .end(LocalDateTime.parse("2050-08-01T08:00:00"))
            .build();

    BookingCreateDto createDto2 = BookingCreateDto.builder()
            .itemId(3L)
            .start(LocalDateTime.parse("2050-07-01T08:00:00"))
            .end(LocalDateTime.parse("2050-08-01T08:00:00"))
            .build();


    @Test
    void setBookingItem() {
        BookingDto bookingDto = bookingService.setBookingItem(createDto1, 1);

        assertThat(bookingDto.getStart(), equalTo(createDto1.getStart()));
        assertThat(bookingDto.getEnd(), equalTo(createDto1.getEnd()));
        assertThat(bookingDto.getItem().getId(), equalTo(createDto1.getItemId()));
        assertThrows(BadRequestException.class, () -> bookingService.setBookingItem(createDto2, 1));
        assertThrows(ConflictException.class, () -> bookingService.setBookingItem(createDto1, 1));
    }

    @Test
    void setBookingStatus() {
         BookingDto bookingDto1 = bookingService.setBookingStatus(2, 2, true);
        BookingDto bookingDto2 = bookingService.setBookingStatus(3, 3, false);

        assertThat(bookingDto1.getStatus(), equalTo(BookingStatus.APPROVED));
        assertThat(bookingDto2.getStatus(), equalTo(BookingStatus.REJECTED));
        assertThrows(BadRequestException.class, () ->
                bookingService.setBookingStatus(1, 3, true));
    }

    @Test
    void getBookingById() {
        long userId = 1L;
        BookingDto bookingDtoCreated = bookingService.setBookingItem(createDto1, userId);
        BookingDto bookingDto = bookingService.getBookingById(userId,bookingDtoCreated.getId());

        assertThat(bookingDto.getStart(), equalTo(createDto1.getStart()));
        assertThat(bookingDto.getEnd(), equalTo(createDto1.getEnd()));
        assertThat(bookingDto.getItem().getId(), equalTo(createDto1.getItemId()));
        assertThat(bookingDto.getBooker().getId(), equalTo(userId));
        assertThrows(ConflictException.class, () ->
                bookingService.getBookingById(999, 1));
    }

    @Test
    void getBookingByBookerIdWhereTime() {
        long bookerId = 1L;
        List<BookingDto> listAll = bookingService.getBookingByBookerIdWhereTime(bookerId, "ALL");
        assertThat(listAll.size(), equalTo(2));
        assertThat(listAll.getFirst().getBooker().getId(), equalTo(1L));
        assertThat(listAll.getFirst().getItem(), notNullValue());
        assertThat(listAll.getLast().getBooker().getId(), equalTo(1L));
        assertThat(listAll.getLast().getItem(), notNullValue());

        List<BookingDto> listPast = bookingService.getBookingByBookerIdWhereTime(bookerId, "PAST");
        assertThat(listPast.size(), equalTo(1));
        assertThat(listPast.getFirst().getBooker().getId(), equalTo(bookerId));
        assertThat(listPast.getLast().getItem(), notNullValue());


        assertThrows(ConflictException.class, () ->
                bookingService.getBookingByBookerIdWhereTime(bookerId, "WAITING"));

        List<BookingDto> listRejected = bookingService.getBookingByBookerIdWhereTime(bookerId, "REJECTED");
        assertThat(listRejected.size(), equalTo(1));
        assertThat(listRejected.getFirst().getBooker().getId(), equalTo(bookerId));
        assertThat(listRejected.getLast().getItem(), notNullValue());

    }

    @Test
    void getBookingByOwnerIdWhereTime() {
        long ownerId = 2L;
        List<BookingDto> listAll = bookingService.getBookingByOwnerIdWhereTime(ownerId, "ALL");
        assertThat(listAll.size(), equalTo(1));
        assertThat(listAll.getFirst().getItem().getOwner().getId(), equalTo(ownerId));
        assertThat(listAll.getFirst().getBooker(), notNullValue());

        assertThrows(ConflictException.class, () ->
                bookingService.getBookingByOwnerIdWhereTime(ownerId, "CURRENT"));

        List<BookingDto> listPast = bookingService.getBookingByOwnerIdWhereTime(ownerId, "PAST");
        assertThat(listPast.size(), equalTo(1));
        assertThat(listPast.getFirst().getItem().getOwner().getId(), equalTo(ownerId));
        assertThat(listPast.getFirst().getBooker(), notNullValue());

        assertThrows(ConflictException.class, () ->
                bookingService.getBookingByOwnerIdWhereTime(ownerId, "FUTURE"));

        List<BookingDto> listFWaiting = bookingService.getBookingByOwnerIdWhereTime(ownerId, "WAITING");
        assertThat(listFWaiting.size(), equalTo(1));
        assertThat(listFWaiting.getFirst().getItem().getOwner().getId(), equalTo(ownerId));
        assertThat(listFWaiting.getFirst().getBooker(), notNullValue());

        assertThrows(ConflictException.class, () ->
                bookingService.getBookingByOwnerIdWhereTime(ownerId, "REJECTED"));
    }
}