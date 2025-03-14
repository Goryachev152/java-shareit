package ru.practicum.shareit.Service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


@Transactional
@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestServiceImplTest {
    private final UserRepository userRepository;
    private final RequestRepository repository;

    @Test
    void saveItemRequest() {
        User user = userRepository.save(new User(1L, "Vladimir", "gorych@mail.ru"));
        ItemRequest itemRequest1 = new ItemRequest(2L, "description", user, LocalDateTime.now());
        ItemRequest itemRequest2 = repository.save(itemRequest1);

        assertThat(itemRequest2.getId(), notNullValue());
        assertThat(itemRequest2.getDescription(), equalTo("description"));
        assertThat(itemRequest2.getRequestor().getId(), equalTo(user.getId()));
    }

}
