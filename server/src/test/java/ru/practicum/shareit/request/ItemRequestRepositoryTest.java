package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.IsNot.not;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "/data.sql")
class ItemRequestRepositoryTest { // Данные для теста класса подготовлены в файле data.sql
    @Autowired
    private ItemRequestRepository repository;

    @Test
    void findAllByRequestorIdSorted() {
        Long requestorId = 2L;
        List<ItemRequest> list = repository.findAllByRequestorIdSorted(requestorId);
        assertThat(list.size(), equalTo(1));
        assertThat(list.getFirst().getRequestor().getId(), equalTo(requestorId));
        assertThat(list.getFirst().getItems().getFirst(), notNullValue());
    }

    @Test
    void findAllByNotRequestorIdSorted() {
        Long requestorId = 2L;
        List<ItemRequest> list = repository.findAllByNotRequestorIdSorted(requestorId);
        assertThat(list.size(), equalTo(2));
        assertThat(list.getFirst().getRequestor().getId(), not(equalTo(requestorId)));
        assertThat(list.getFirst().getItems().getFirst(), notNullValue());
        assertThat(list.getLast().getRequestor().getId(), not(equalTo(requestorId)));
        assertThat(list.getLast().getItems().getFirst(), notNullValue());
    }
}