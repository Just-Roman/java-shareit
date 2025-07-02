package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Проверка на возможность оставить отзыв на вещь после успешной брони (Добавлять отзыв может только тот, кто брал вещь в аренду)
    @Query(value = """  
            SELECT EXISTS (
                SELECT 1 FROM booking
                WHERE item_id = :itemId
                AND booker_id = :userId
                AND status = 'APPROVED'
                AND end_time < CURRENT_TIMESTAMP + INTERVAL '1' SECOND
            )""",
            nativeQuery = true)
    boolean hasUserBookedItem(@Param("userId") Long userId, @Param("itemId") Long itemId);

    // Проверка свободных дат для новой брони
    @Query("""
            SELECT COUNT(b) > 0
            FROM Booking b
            WHERE b.item.id = :itemId
            AND b.status IN ('APPROVED', 'WAITING')
            AND (
                (b.start BETWEEN :start AND :end) OR
                (b.end BETWEEN :start AND :end) OR
                (b.start <= :start AND b.end >= :end)
            )
            """)
    boolean existsByItemIdAndTimeRange(@Param("itemId") Long itemId,
                                       @Param("start") LocalDateTime start,
                                       @Param("end") LocalDateTime end);

    // Получение списка всех бронирований (с учетом их статуса) текущего пользователя
    @Query(value = """
            SELECT * FROM booking
            WHERE booker_id = :bookerId
            AND (
                (:state = 'ALL') OR
                (:state = 'CURRENT' AND start_time <= NOW() AND end_time >= NOW()) OR
                (:state = 'PAST' AND end_time < NOW()) OR
                (:state = 'FUTURE' AND start_time > NOW()) OR
                (:state = 'WAITING' AND status = 'WAITING') OR
                (:state = 'REJECTED' AND status = 'REJECTED')
            )
            ORDER BY start_time DESC""",
            nativeQuery = true)
    List<Booking> getBookingByBookerIdWhereTime(@Param("bookerId") Long bookerId, @Param("state") String state);


    // Получение списка бронирований для всех вещей текущего пользователя.
    @Query(value = """
            SELECT b.* FROM booking b
            JOIN items i ON b.item_id = i.id
            WHERE i.owner_id = :ownerId
            AND (
                (:state = 'ALL') OR
                (:state = 'CURRENT' AND start_time <= NOW() AND end_time >= NOW()) OR
                (:state = 'PAST' AND b.end_time < NOW()) OR
                (:state = 'FUTURE' AND b.start_time > NOW()) OR
                (:state = 'WAITING' AND status = 'WAITING') OR
                (:state = 'REJECTED' AND status = 'REJECTED')
            )
            ORDER BY b.start_time DESC""",
            nativeQuery = true)
    List<Booking> getBookingByOwnerIdWhereTime(@Param("ownerId") Long ownerId, @Param("state") String state);


}
