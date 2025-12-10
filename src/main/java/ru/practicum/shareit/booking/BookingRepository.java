package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    // For booker
    List<Booking> findByBookerIdOrderByStartDesc(Long bookerId);

    @Query("select b from Booking b where b.booker.id = :bookerId and b.start <= :now and b.end >= :now order by b.start desc")
    List<Booking> findCurrentByBooker(@Param("bookerId") Long bookerId, @Param("now") LocalDateTime now);

    List<Booking> findByBookerIdAndEndBeforeOrderByStartDesc(Long bookerId, LocalDateTime end);

    List<Booking> findByBookerIdAndStartAfterOrderByStartDesc(Long bookerId, LocalDateTime start);

    List<Booking> findByBookerIdAndStatusOrderByStartDesc(Long bookerId, BookingStatus status);

    // For owner (items owned by user)
    @Query("select b from Booking b where b.item.ownerId = :ownerId order by b.start desc")
    List<Booking> findAllByOwnerOrderByStartDesc(@Param("ownerId") Long ownerId);

    @Query("select b from Booking b where b.item.ownerId = :ownerId and b.start <= :now and b.end >= :now order by b.start desc")
    List<Booking> findCurrentByOwner(@Param("ownerId") Long ownerId, @Param("now") LocalDateTime now);

    @Query("select b from Booking b where b.item.ownerId = :ownerId and b.end < :now order by b.start desc")
    List<Booking> findPastByOwner(@Param("ownerId") Long ownerId, @Param("now") LocalDateTime now);

    @Query("select b from Booking b where b.item.ownerId = :ownerId and b.start > :now order by b.start desc")
    List<Booking> findFutureByOwner(@Param("ownerId") Long ownerId, @Param("now") LocalDateTime now);

    @Query("select b from Booking b where b.item.ownerId = :ownerId and b.status = :status order by b.start desc")
    List<Booking> findByOwnerAndStatus(@Param("ownerId") Long ownerId, @Param("status") BookingStatus status);

    // For last/next booking
    Booking findTopByItemIdAndStatusAndStartLessThanEqualOrderByStartDesc(Long itemId, BookingStatus status, LocalDateTime now);

    Booking findTopByItemIdAndStatusAndStartGreaterThanOrderByStartAsc(Long itemId, BookingStatus status, LocalDateTime now);

    // Bulk helpers to avoid N+1 when building owner view
    @Query("select b from Booking b " +
            "where b.item.id in :itemIds and b.status = :status and b.start <= :now " +
            "order by b.item.id asc, b.start desc")
    List<Booking> findLastForItems(@Param("itemIds") List<Long> itemIds,
                                   @Param("status") BookingStatus status,
                                   @Param("now") LocalDateTime now);

    @Query("select b from Booking b " +
            "where b.item.id in :itemIds and b.status = :status and b.start > :now " +
            "order by b.item.id asc, b.start asc")
    List<Booking> findNextForItems(@Param("itemIds") List<Long> itemIds,
                                   @Param("status") BookingStatus status,
                                   @Param("now") LocalDateTime now);
}


