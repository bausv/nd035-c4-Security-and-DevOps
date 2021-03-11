package com.example.demo.model.persistence.repositories;

import com.example.demo.model.persistence.Item;
import junit.framework.TestCase;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemRepositoryTest extends TestCase {

    @Autowired
    private ItemRepository itemRepository;
    private Item item;

    @Before
    public void setup() {
        item = new Item();
        item.setName("anItem");
        item.setDescription("a description");
        item.setPrice(new BigDecimal(1.2f));
        item = itemRepository.save(item);
    }

    @Test
    public void testFindByName() {
        List<Item> byName = itemRepository.findByName(item.getName());
        assertThat(byName).isNotNull().isNotEmpty().hasSize(1);
        assertThat(byName).usingElementComparatorOnFields("name").containsExactlyInAnyOrderElementsOf(Arrays.asList(item));
        assertThat(byName.get(0)).isEqualTo(item);
    }
}