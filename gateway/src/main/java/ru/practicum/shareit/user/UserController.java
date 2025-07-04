package ru.practicum.shareit.user;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;


    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Validated UserCreateDto userDto) {
        log.info("Creating user {}", userDto);
        return userClient.create(userDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> update(@PathVariable Long userId,
                                         @RequestBody @Validated UserUpdateDto userDto) {
        log.info("Update user {}, userId={}", userDto, userId);
        return userClient.update(userId, userDto);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable Long userId) {
        log.info("Get userId {}", userId);
        return userClient.getUserById(userId);
    }

    @GetMapping()
    public ResponseEntity<Object> getAll(@Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Get getAll  size{}", size);
        return userClient.getAll(size);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        log.info("Delete User  userId{}", userId);
        userClient.deleteUser(userId);
    }

}
