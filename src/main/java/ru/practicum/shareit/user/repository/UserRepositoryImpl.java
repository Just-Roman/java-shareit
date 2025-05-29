package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserRepositoryImpl implements UserRepository {
    private long id = 1L;
    private final Map<Long, User> userMap = new HashMap<>();

    public long getNextId() {
        return id++;
    }

    @Override
    public User create(User user) {
        checkCloneEmail(user.getEmail());
        long id = getNextId();
        user.setId(id);
        userMap.put(id, user);
        return user;
    }

    @Override
    public User update(User user) {
        long id = user.getId();
        checkUserId(id);
        User userSaved = userMap.get(id);

        if (user.getName() != null) {
            userSaved.setName(user.getName());
        }
        if (user.getEmail() != null) {
            String email = user.getEmail();
            checkCloneEmail(email);
            userSaved.setEmail(email);
        }
        return userSaved;
    }

    @Override
    public User getUserById(long id) {
        checkUserId(id);
        return userMap.get(id);
    }

    @Override
    public Collection<User> getAll() {
        return userMap.values();
    }

    @Override
    public void deleteUser(long userId) {
        userMap.remove(userId);
    }

    private void checkCloneEmail(String email) {
        List<User> users = userMap.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .toList();
        if (!users.isEmpty()) {
            throw new ConflictException("email уже существует");
        }
    }

    private void checkUserId(long id) {
        if (!userMap.containsKey(id)) {
            throw new NotFoundException("User отсутствует");
        }
    }

}
