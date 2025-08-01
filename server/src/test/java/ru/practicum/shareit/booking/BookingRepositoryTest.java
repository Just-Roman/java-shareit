package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.booking.dto.BookingCreateDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {"/data/cleanup.sql", "/data/data.sql"})
class BookingRepositoryTest { // Данные для теста класса подготовлены в файле data.sql
    @Autowired
    private BookingRepository bookingRepository;

    @Test
    void hasUserBookedItem() {
        assertThat(bookingRepository.hasUserBookedItem(2L, 1L), equalTo(true));
        assertThat(bookingRepository.hasUserBookedItem(3L, 2L), equalTo(false));
    }

    @Test
    void existsByItemIdAndTimeRange() {
        BookingCreateDto dto1 = BookingCreateDto.builder()
                .itemId(3L)
                .start(LocalDateTime.parse("2025-07-10T22:00:00"))
                .end(LocalDateTime.parse("2025-07-10T22:00:00"))
                .build();

        BookingCreateDto dto2 = BookingCreateDto.builder()
                .itemId(4L)
                .start(LocalDateTime.parse("2025-05-10T20:00:00"))
                .end(LocalDateTime.parse("2025-05-11T20:00:00"))
                .build();

        assertThat(bookingRepository.existsByItemIdAndTimeRange(dto1.getItemId(), dto1.getStart(), dto1.getEnd()),
                equalTo(false));  // Этот промежуток времени свободен, бронь состоится
        assertThat(bookingRepository.existsByItemIdAndTimeRange(dto2.getItemId(), dto2.getStart(), dto2.getEnd()),
                equalTo(true));  // Этот промежуток времени занят, бронь невозможна
    }

    @Test
    void getBookingByBookerIdWhereTime() {
        Long bookerId = 1L;
        List<Booking> listAll = bookingRepository.getBookingByBookerIdWhereTime(bookerId, "ALL");
        assertThat(listAll.size(), equalTo(2));
        assertThat(listAll.getFirst().getBooker().getId(), equalTo(1L));
        assertThat(listAll.getFirst().getItem(), notNullValue());
        assertThat(listAll.getLast().getBooker().getId(), equalTo(1L));
        assertThat(listAll.getLast().getItem(), notNullValue());

        List<Booking> listPast = bookingRepository.getBookingByBookerIdWhereTime(bookerId, "PAST");
        assertThat(listPast.size(), equalTo(1));
        assertThat(listPast.getFirst().getBooker().getId(), equalTo(bookerId));
        assertThat(listPast.getLast().getItem(), notNullValue());

        List<Booking> listFWaiting = bookingRepository.getBookingByBookerIdWhereTime(bookerId, "WAITING");
        assertThat(listFWaiting.size(), equalTo(0));

        List<Booking> listRejected = bookingRepository.getBookingByBookerIdWhereTime(bookerId, "REJECTED");
        assertThat(listRejected.size(), equalTo(1));
        assertThat(listRejected.getFirst().getBooker().getId(), equalTo(bookerId));
        assertThat(listRejected.getLast().getItem(), notNullValue());
    }

    @Test
    void getBookingByOwnerIdWhereTime() {
        Long ownerId = 2L;
        List<Booking> listAll = bookingRepository.getBookingByOwnerIdWhereTime(ownerId, "ALL");
        assertThat(listAll.size(), equalTo(2));
        assertThat(listAll.getFirst().getItem().getOwner().getId(), equalTo(ownerId));
        assertThat(listAll.getFirst().getBooker(), notNullValue());
        assertThat(listAll.getLast().getItem().getOwner().getId(), equalTo(ownerId));
        assertThat(listAll.getLast().getBooker(), notNullValue());

        List<Booking> listCurrent = bookingRepository.getBookingByOwnerIdWhereTime(ownerId, "CURRENT");
        assertThat(listCurrent.size(), equalTo(0));

        List<Booking> listPast = bookingRepository.getBookingByOwnerIdWhereTime(ownerId, "PAST");
        assertThat(listPast.size(), equalTo(2));
        assertThat(listPast.getFirst().getItem().getOwner().getId(), equalTo(ownerId));
        assertThat(listPast.getFirst().getBooker(), notNullValue());
        assertThat(listPast.getLast().getItem().getOwner().getId(), equalTo(ownerId));
        assertThat(listPast.getLast().getBooker(), notNullValue());

        List<Booking> listFuture = bookingRepository.getBookingByOwnerIdWhereTime(ownerId, "FUTURE");
        assertThat(listFuture.size(), equalTo(0));

        List<Booking> listFWaiting = bookingRepository.getBookingByOwnerIdWhereTime(ownerId, "WAITING");
        assertThat(listFWaiting.size(), equalTo(1));
        assertThat(listFWaiting.getFirst().getItem().getOwner().getId(), equalTo(ownerId));
        assertThat(listFWaiting.getFirst().getBooker(), notNullValue());

        List<Booking> listRejected = bookingRepository.getBookingByOwnerIdWhereTime(ownerId, "REJECTED");
        assertThat(listRejected.size(), equalTo(0));
    }
}