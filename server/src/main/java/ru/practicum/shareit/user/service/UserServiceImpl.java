package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.Collection;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto create(UserCreateDto userDto) {
        User user = userMapper.createDtoToModel(userDto);
        return userMapper.modelToDto(userRepository.save(user));
    }

    @Override
    public UserDto update(Long userId, UserUpdateDto userDto) {
        User user = checkAndReturnUser(userId);

        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
        return userMapper.modelToDto(userRepository.save(user));
    }

    @Override
    public UserDto getUserById(long id) {
        return userMapper.modelToDto(checkAndReturnUser(id));
    }

    @Override
    public List<UserDto> getAll() {
        return userMapper.listModelToDto(userRepository.findAll());
    }

    @Override
    public void deleteUser(long userId) {
        userRepository.deleteById(userId);
    }

    private User checkAndReturnUser(long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User не найден"));
    }
}
