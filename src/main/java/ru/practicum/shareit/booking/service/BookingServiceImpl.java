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
import java.util.Optional;

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
        User user = validNotFoundUser(userId);
        Item item = validNotFoundItem(request.getItemId());
        Booking booking = BookingMapper.mapToBooking(request, user, item);
        booking.setStatus(Status.WAITING);
        bookingRepository.save(booking);
        log.info("Бронирование с id {} добавлено в сервис", booking.getId());
        return BookingMapper.mapToBookingDto(booking);
    }

    @Transactional
    @Override
    public BookingDto confirmationBooking(Long userId, Long bookingId, boolean approved) {
        Booking booking = validNotFoundBooking(bookingId);
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
        User user = validNotFoundUser(userId);
        Booking booking = validNotFoundBooking(bookingId);
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
        User booker = validNotFoundUser(bookerId);
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
        User owner = validNotFoundUser(ownerId);
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

    private User validNotFoundUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        return user.get();
    }

    private Item validNotFoundItem(Long itemId) {
        Optional<Item> item = itemRepository.findById(itemId);
        if (item.isPresent()) {
            if (item.get().getAvailable().equals(false)) {
                throw  new ValidationException("Вещь с id " + itemId + " недоступна для бронирования");
            }
            return item.get();
        } else {
            throw new NotFoundException("Вещь с id " + itemId + " не найдена");
        }
    }

    private Booking validNotFoundBooking(Long bookingId) {
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        if (booking.isPresent()) {
            return booking.get();
        } else {
            throw new NotFoundException("Бронирование с id " + bookingId + " не найдено");
        }
    }
}
