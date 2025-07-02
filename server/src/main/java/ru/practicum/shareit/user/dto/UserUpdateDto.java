package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserUpdateDto {
    private String name;
    private String email;
}
