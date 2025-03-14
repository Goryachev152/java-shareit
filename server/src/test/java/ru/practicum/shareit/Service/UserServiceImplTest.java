package ru.practicum.shareit.Service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DataJpaTest
public class UserServiceImplTest {
    private final UserRepository repository;

    @Test
    void saveUser() {
        User user = new User(1L, "Vladimir", "gorych@mail.ru");

        repository.save(user);

        assertThat(user.getId(), notNullValue());
        assertThat(user.getName(), equalTo("Vladimir"));
        assertThat(user.getEmail(), equalTo("gorych@mail.ru"));

    }
}
