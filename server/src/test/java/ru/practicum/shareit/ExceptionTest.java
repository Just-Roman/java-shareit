package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.ErrorResponse;
import ru.practicum.shareit.exception.InternalServerErrorException;
import ru.practicum.shareit.exception.ValidationException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ExceptionTest {

    @Test
    void contextLoads() {
    }

    @Test
    void errorResponseFieldsShouldBeSetCorrectly() {
        ErrorResponse response = new ErrorResponse("Ошибка", "Описание ошибки");
        assertEquals("Ошибка", response.getError());
        assertEquals("Описание ошибки", response.getDescription());
    }

    @Test
    void validationExceptionShouldStoreMessage() {
        ValidationException ex = new ValidationException("Некорректные данные");
        assertEquals("Некорректные данные", ex.getMessage());
    }

    @Test
    void internalServerErrorExceptionShouldStoreMessage() {
        InternalServerErrorException ex = new InternalServerErrorException("Внутренняя ошибка");
        assertEquals("Внутренняя ошибка", ex.getMessage());
    }

}
