package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.client.BaseClient;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> create(BookItemRequestDto dto, Long userId) {
        return post("", userId, dto);
    }

    public ResponseEntity<Object> confirmationBooking(Long userId, Long bookingId, boolean approved) {
        return patch("/" + bookingId + "?approved=" + approved, userId, null, null);
    }

    public ResponseEntity<Object> findById(Long bookingId, Long userId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> findAllByUser(Long userId, String state) {
        return get("?state=" + state, userId);
    }

    public ResponseEntity<Object> findAllByUserItems(Long userId, String state) {
        return get("/owner?state=" + state, userId);
    }
}
