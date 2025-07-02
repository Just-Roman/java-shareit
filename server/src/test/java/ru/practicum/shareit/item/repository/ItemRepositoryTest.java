package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.item.dto.ItemCommentDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {"/data/cleanup.sql", "/data/data.sql"})
class ItemRepositoryTest {  // Данные для теста класса подготовлены в файле data.sql
    @Autowired
    private ItemRepository itemRepository;

    @Test
    void findItemWithBookings() {
        ItemCommentDto itemCommentDto = ItemCommentDto.builder()
                .id(2L)
                .name("Лопата")
                .description("Совковая лопата")
                .available(true)
                .build();

        ItemCommentDto query = itemRepository.findItemWithBookings(itemCommentDto.getId()).get();

        assertThat(query.getId(), equalTo(itemCommentDto.getId()));
        assertThat(query.getName(), equalTo(itemCommentDto.getName()));
        assertThat(query.getDescription(), equalTo(itemCommentDto.getDescription()));
        assertThat(query.getAvailable(), equalTo(itemCommentDto.getAvailable()));
        assertThat(query.getOwner(), notNullValue());
    }

    @Test
    void allItemByOwnerIdWithComment() {
        ItemCommentDto itemCommentDto1 = ItemCommentDto.builder()
                .id(2L)
                .name("Лопата")
                .description("Совковая лопата")
                .available(true)
                .build();

        ItemCommentDto itemCommentDto2 = ItemCommentDto.builder()
                .id(4L)
                .name("Рогатка")
                .description("Деревянная рогатка")
                .available(true)
                .build();

        List<ItemCommentDto> list = itemRepository.allItemByOwnerIdWithComment(2L); // Сортировка по Id
        ItemCommentDto dtoTest1 = list.getFirst();
        ItemCommentDto dtoTest2 = list.getLast();

        assertThat(list.size(), equalTo(2));
        assertThat(dtoTest1.getId(), equalTo(itemCommentDto1.getId()));
        assertThat(dtoTest1.getName(), equalTo(itemCommentDto1.getName()));
        assertThat(dtoTest1.getDescription(), equalTo(itemCommentDto1.getDescription()));
        assertThat(dtoTest1.getAvailable(), equalTo(itemCommentDto1.getAvailable()));
        assertThat(dtoTest1.getOwner(), notNullValue());

        assertThat(dtoTest2.getId(), equalTo(itemCommentDto2.getId()));
        assertThat(dtoTest2.getName(), equalTo(itemCommentDto2.getName()));
        assertThat(dtoTest2.getDescription(), equalTo(itemCommentDto2.getDescription()));
        assertThat(dtoTest2.getAvailable(), equalTo(itemCommentDto2.getAvailable()));
        assertThat(dtoTest2.getOwner(), notNullValue());
    }

    @Test
    void getItemsByNameOrDescription() {
        Item model = Item.builder()
                .name("Дрель")
                .description("Аккумуляторная дрель")
                .available(true)
                .build();

        List<Item> list = itemRepository.getItemsByNameOrDescription("дрель");
        Item itemQuery = list.getFirst();

        assertThat(list.size(), equalTo(1));
        assertThat(itemQuery.getName(), equalTo(model.getName()));
        assertThat(itemQuery.getDescription(), equalTo(model.getDescription()));
        assertThat(itemQuery.getAvailable(), equalTo(model.getAvailable()));
        assertThat(itemQuery.getOwner(), notNullValue());
        assertThat(itemQuery.getRequest(), notNullValue());
    }
}