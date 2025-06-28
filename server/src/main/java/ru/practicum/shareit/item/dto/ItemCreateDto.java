package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemCreateDto {
    private String name;
    private String description;
    private Boolean available;
    private Long owner;
    private Long requestId;
}
