package ru.practicum.shareit.Service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(classes = ShareItServer.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ItemServiceImplTest {
    private final ItemRepository repository;
    private final UserRepository userRepository;

    @Test
    void saveItem() {
        User owner = userRepository.save(new User(1L, "f", "f@mail.ru"));
        Item item = new Item(1L, "name", "description", true, owner, null);
        repository.save(item);
        assertThat(item.getId(), notNullValue());
        assertThat(item.getName(), equalTo("name"));
        assertThat(item.getDescription(), equalTo("description"));
        assertThat(item.getAvailable(), equalTo(true));
        assertThat(item.getOwner(), equalTo(owner));
        assertThat(item.getRequest(), equalTo(null));

    }
}
