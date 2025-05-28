package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.User;

import java.util.Collection;

public interface UserRepository {
    User create(User user);

    User update(User user);

    User getUserById(long id);

    Collection<User> getAll();

    void deleteUser(long userId);
}
