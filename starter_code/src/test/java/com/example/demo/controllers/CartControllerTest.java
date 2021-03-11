package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
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

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CartControllerTest extends AbstractUserCenteredTestCase {

    @Autowired
    private CartController cartController;

    @Before
    public void setup() {
        super.setup();
    }

    @Test
    public void testAddToCart() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername(user.getUsername());
        request.setItemId(1l);
        request.setQuantity(2);
        ResponseEntity<Cart> cartResponseEntity = cartController.addTocart(request);
        assertThat(cartResponseEntity).isNotNull();
        assertThat(cartResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(cartResponseEntity.getBody()).isNotNull();
        assertThat(cartResponseEntity.getBody().getItems()).isNotNull().isNotEmpty().hasSize(2);
    }

    @Test
    public void testAddToCartNonExistingUser() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("does not exist");
        request.setItemId(1l);
        request.setQuantity(2);
        ResponseEntity<Cart> cartResponseEntity = cartController.addTocart(request);
        assertThat(cartResponseEntity).isNotNull();
        assertThat(cartResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(cartResponseEntity.getBody()).isNull();
    }

    @Test
    public void testAddToCartNonExistingItem() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername(user.getUsername());
        request.setItemId(9999l);
        request.setQuantity(2);
        ResponseEntity<Cart> cartResponseEntity = cartController.addTocart(request);
        assertThat(cartResponseEntity).isNotNull();
        assertThat(cartResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(cartResponseEntity.getBody()).isNull();
    }

    @Test
    public void testRemoveFromcart() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername(user.getUsername());
        request.setItemId(1l);
        request.setQuantity(3);
        ResponseEntity<Cart> cartResponseEntity = cartController.addTocart(request);
        assertThat(cartResponseEntity).isNotNull();
        assertThat(cartResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(cartResponseEntity.getBody()).isNotNull();
        assertThat(cartResponseEntity.getBody().getItems()).isNotNull().isNotEmpty().hasSize(3);

        request.setQuantity(1);
        cartResponseEntity = cartController.removeFromcart(request);
        assertThat(cartResponseEntity).isNotNull();
        assertThat(cartResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(cartResponseEntity.getBody()).isNotNull();
        assertThat(cartResponseEntity.getBody().getItems()).isNotNull().isNotEmpty().hasSize(2);
    }
}