package ru.practicum.shareit.Service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(classes = ShareItServer.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class BookingServiceImplTest {
    private final BookingRepository repository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;


    @Test
    void saveBooking() {
        User user = userRepository.save(new User(1L, "Vladimir", "gorych@mail.ru"));
        User owner = userRepository.save((new User(2L, "anddrey", "gorych152@mail.ru")));
        Item item = itemRepository.save(new Item(1L, "name", "description", true, owner, null));
        Booking booking1 = new Booking(2L, LocalDateTime.now(), LocalDateTime.now().plusMinutes(5),
                item, user, Status.WAITING);
        Booking booking2 = repository.save(booking1);

        assertThat(booking2.getId(), notNullValue());
        assertThat(booking2.getBooker().getId(), equalTo(user.getId()));
        assertThat(booking2.getStatus().toString(), equalTo("WAITING"));
    }
}
