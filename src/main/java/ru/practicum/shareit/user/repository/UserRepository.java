package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserRepository {
    private final Map<Long, User> userMap = new HashMap<>();

    public User createUser(User user) {
        user = user.toBuilder()
                .id(getNextId())
                .build();
        userMap.put(user.getId(), user);
        return user;
    }

    public User updateUser(User user, Long userId) {
        userMap.put(userId, user);
        return user;
    }

    public List<User> getAllUsers() {
        return userMap.values().stream().toList();
    }

    public Optional<User> getByIdUser(Long userId) {
        return Optional.ofNullable(userMap.get(userId));
    }

    public User deleteByUserId(Long userId) {
        return userMap.remove(userId);
    }

    private Long getNextId() {
        long currentMaxId = userMap.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
