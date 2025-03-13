package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;


@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody BookItemRequestDto dto,
                                         @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingClient.create(dto, userId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> confirmationBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                       @PathVariable Long bookingId, @RequestParam boolean approved) {
        return bookingClient.confirmationBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> findById(@PathVariable Long bookingId,
                                           @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingClient.findById(bookingId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> findAllByUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @RequestParam(name = "state", defaultValue = "ALL") String state) {
        return bookingClient.findAllByUser(userId, state);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> findAllByUserItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                     @RequestParam(name = "state", defaultValue = "ALL") String state) {
        return bookingClient.findAllByUserItems(userId, state);
    }
}
