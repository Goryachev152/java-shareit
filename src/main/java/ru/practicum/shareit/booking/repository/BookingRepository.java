package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBookerIdOrderByStartDesc(Long bookerId);

    @Query("select b from Booking b where b.booker = ?1 " +
            "and b.end > ?2 and start < ?2 order by b.start")
    List<Booking> findByBookerIdAndStatusCurrent(Long bookerId, LocalDateTime now);

    @Query("select b from Booking b where b.booker = ?1 " +
            "and b.end < ?2 order by b.start")
    List<Booking> findByBookerIdAndStatusPast(Long bookerId, LocalDateTime now);

    @Query("select b from Booking b where b.booker = ?1 " +
            "and b.start > ?2 order by b.start")
    List<Booking> findByBookerIdAndStatusFuture(Long bookerId, LocalDateTime now);

    List<Booking> findByBookerIdAndStatusOrderByStartDesc(Long bookerId, Status status);

    @Query("select b from Booking b, Item i where i.owner = ?1 order by b.start")
    List<Booking> findByOwnerIdOrderByStartDesc(Long ownerId);

    @Query("select b from Booking b, Item i where i.owner = ?1 " +
            "and b.end > ?2 and start < ?2 order by b.start")
    List<Booking> findByOwnerIdAndStatusCurrent(Long ownerId, LocalDateTime now);

    @Query("select b from Booking b, Item i where i.owner = ?1 " +
            "and b.end < ?2 order by b.start")
    List<Booking> findByOwnerIdAndStatusPast(Long ownerId, LocalDateTime now);

    @Query("select b from Booking b, Item i where i.owner = ?1 " +
            "and b.start > ?2 order by b.start")
    List<Booking> findByOwnerIdAndStatusFuture(Long ownerId, LocalDateTime now);

    @Query("select b from Booking b, Item i where i.owner = ?1 and status = ?2 order by b.start")
    List<Booking> findByOwnerIdAndStatusOrderByStartDesc(Long ownerId, Status status);

    @Query("select b from Booking b where b.item = ?1 and b.end < ?2")
    Booking findByItemIdAndEndTimeBefore(Item item, LocalDateTime created);

    @Query("select b from Booking as b where b.item.id in :itemIds")
    List<Booking> findAllByItemIdOrderByStartDesc(@Param("itemIds") List<Long> itemIds);
}
