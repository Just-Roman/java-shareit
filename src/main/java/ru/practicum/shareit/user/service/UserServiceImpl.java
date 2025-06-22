package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User create(User user) {
        return userRepository.save(user);
    }

    @Override
    public User update(User newUser) {
        User user = checkAndReturnUser(newUser.getId());

        if (newUser.getName() != null) {
            user.setName(newUser.getName());
        }
        if (newUser.getEmail() != null) {
            user.setEmail(newUser.getEmail());
        }

        return userRepository.save(user);
    }

    @Override
    public User getUserById(long id) {
        return checkAndReturnUser(id);
    }

    @Override
    public Collection<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(long userId) {
        userRepository.deleteById(userId);
    }

    private User checkAndReturnUser(long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User не найден"));
    }
}
