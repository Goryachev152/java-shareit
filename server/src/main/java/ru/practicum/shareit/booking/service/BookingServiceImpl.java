package ru.practicum.shareit.booking.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Transactional
    @Override
    public BookingDto addBooking(Long userId, BookingDtoRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь " +
                "с id = " + userId + " не найден"));
        Item item = itemRepository.findById(request.getItemId()).orElseThrow(() -> new NotFoundException("Вещь " +
                "с id = " + request.getItemId() + " не найдена"));
        if (!item.getAvailable()) {
            throw new ValidationException("Вещь с id " + item.getId() + " недоступна для бронирования");
        }
        Booking booking = BookingMapper.mapToBooking(request, user, item);
        booking.setStatus(Status.WAITING);
        bookingRepository.save(booking);
        log.info("Бронирование с id {} добавлено в сервис", booking.getId());
        log.info("start = {} / end = {}", booking.getStart(), booking.getEnd());
        return BookingMapper.mapToBookingDto(booking);
    }

    @Transactional
    @Override
    public BookingDto confirmationBooking(Long userId, Long bookingId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("Вещь " +
                "с id = " + bookingId + " не найдена"));
        Long owner = booking.getItem().getOwner().getId();
        Long itemId = booking.getItem().getId();
        if (!owner.equals(userId)) {
            throw new ValidationException("Пользователь с id " + userId + " не является владельцем вещи" + itemId);
        }
        if (approved) {
            booking.setStatus(Status.APPROVED);
            log.info("Бронирование с id {} одобрено", booking.getId());
        } else {
            booking.setStatus(Status.REJECTED);
            log.info("Бронирование с id {} отклонено", booking.getId());
        }
        return BookingMapper.mapToBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto findBookingById(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("Вещь " +
                "с id = " + bookingId + " не найдена"));
        Long owner = booking.getItem().getOwner().getId();
        Long booker = booking.getBooker().getId();
        Long itemId = booking.getItem().getId();
        if (owner.equals(userId) || booker.equals(userId)) {
            return BookingMapper.mapToBookingDto(booking);
        } else {
            throw new ValidationException("Пользователь с id " + userId +
                    " не является владельцем вещи или автором бронирования" + itemId);
        }
    }

    @Override
    public List<BookingDto> findBookingItemByUserId(Long bookerId, String state) {
        userRepository.findById(bookerId).orElseThrow(() -> new NotFoundException("Пользователь " +
                "с id = " + bookerId + " не найден"));
        LocalDateTime now = LocalDateTime.now();
        List<Booking> result = switch (state) {
            case "ALL" -> bookingRepository.findByBookerIdOrderByStartDesc(bookerId);
            case "CURRENT" -> bookingRepository.findByBookerIdAndStatusCurrent(bookerId, now);
            case "PAST" -> bookingRepository.findByBookerIdAndStatusPast(bookerId, now);
            case "FUTURE" -> bookingRepository.findByBookerIdAndStatusFuture(bookerId, now);
            case "WAITING" -> bookingRepository.findByBookerIdAndStatusOrderByStartDesc(bookerId, Status.WAITING);
            case "REJECTED" -> bookingRepository.findByBookerIdAndStatusOrderByStartDesc(bookerId, Status.REJECTED);
            default -> throw new ValidationException("Некорректный статус");
        };
        return result.stream()
                .map(BookingMapper::mapToBookingDto)
                .toList();
    }

    @Override
    public List<BookingDto> findBookingItemByOwnerId(Long ownerId, String state) {
        userRepository.findById(ownerId).orElseThrow(() -> new NotFoundException("Пользователь " +
                "с id = " + ownerId + " не найден"));
        LocalDateTime now = LocalDateTime.now();
        List<Booking> result = switch (state) {
            case "ALL" -> bookingRepository.findByOwnerIdOrderByStartDesc(ownerId);
            case "CURRENT" -> bookingRepository.findByOwnerIdAndStatusCurrent(ownerId, now);
            case "PAST" -> bookingRepository.findByOwnerIdAndStatusPast(ownerId, now);
            case "FUTURE" -> bookingRepository.findByOwnerIdAndStatusFuture(ownerId, now);
            case "WAITING" -> bookingRepository.findByOwnerIdAndStatusOrderByStartDesc(ownerId, Status.WAITING);
            case "REJECTED" -> bookingRepository.findByOwnerIdAndStatusOrderByStartDesc(ownerId, Status.REJECTED);
            default -> throw new ValidationException("Некорректный статус");
        };
        return result.stream()
                .map(BookingMapper::mapToBookingDto)
                .toList();
    }
}
