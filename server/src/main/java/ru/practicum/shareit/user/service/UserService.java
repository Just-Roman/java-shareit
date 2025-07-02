package ru.practicum.shareit.user.service;

import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.Collection;
import java.util.List;

public interface UserService {
    UserDto create(UserCreateDto userDto);

    UserDto update(Long userId, UserUpdateDto userDto);

    UserDto getUserById(long id);

    List<UserDto> getAll();

    void deleteUser(long userId);
}
