package com.example.demo.controllers;

import com.example.demo.AbstractBaseTestCase;
import com.example.demo.model.persistence.Item;
import junit.framework.TestCase;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemControllerTest extends AbstractBaseTestCase {

    @Autowired
    private ItemController itemController;

    @Override
    @Before
    public void setup() {
        super.setup();
    }

    @Test
    public void testGetItems() {
        Item item1 = new Item();
        item1.setName("Round Widget");
        Item item2 = new Item();
        item2.setName("Square Widget");
        List<Item> expectedItems = Arrays.asList(item1, item2);
        ResponseEntity<List<Item>> items = itemController.getItems();
        assertThat(items).isNotNull();
        assertThat(items.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(items.getBody()).isNotNull().isNotEmpty().usingElementComparatorOnFields("name").containsOnlyElementsOf(expectedItems);
    }

    @Test
    public void testGetItemById() {
        ResponseEntity<Item> itemById = itemController.getItemById(1l);
        assertThat(itemById).isNotNull();
        assertThat(itemById.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(itemById.getBody()).isNotNull();
        assertThat(itemById.getBody().getId()).isEqualTo(1l);
    }

    @Test
    public void testGetItemsByName() {
        ResponseEntity<List<Item>> itemsByName = itemController.getItemsByName("Round Widget");
        assertThat(itemsByName).isNotNull();
        assertThat(itemsByName.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(itemsByName.getBody()).isNotNull().isNotEmpty();
        assertThat(itemsByName.getBody().get(0).getId()).isEqualTo(1l);
        assertThat(itemsByName.getBody().get(0).getName()).isEqualTo("Round Widget");
    }
}