package ru.practicum.shareit.request.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Getter
@Setter
public class ItemRequestCreateDto {
    @NotNull
    @NotBlank
    private String description;
}
