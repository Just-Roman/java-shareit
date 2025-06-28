package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.User;

import java.util.Collection;

public interface UserService {
    User create(User user);

    User update(User user);

    User getUserById(long id);

    Collection<User> getAll();

    void deleteUser(long userId);
}
