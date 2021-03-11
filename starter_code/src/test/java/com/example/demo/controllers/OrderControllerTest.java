package com.example.demo.controllers;

import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import org.assertj.core.data.Offset;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderControllerTest extends AbstractUserCenteredTestCase {

    @Autowired
    private OrderController orderController;

    @Autowired
    private OrderRepository orderRepository;

    @Before
    @Override
    public void setup() {
        super.setup();
    }

    @Test
    public void testSubmit() {
        ResponseEntity<UserOrder> orderResponseEntity = orderController.submit(user.getUsername());
        assertThat(orderResponseEntity).isNotNull();
        assertThat(orderResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(orderResponseEntity.getBody()).isNotNull();
    }

    @Test
    public void testSubmitNonExistingUser() {
        ResponseEntity<UserOrder> orderResponseEntity = orderController.submit("does not exist");
        assertThat(orderResponseEntity).isNotNull();
        assertThat(orderResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(orderResponseEntity.getBody()).isNull();
    }

    @Test
    public void testGetOrdersForUser() {
        UserOrder entity = new UserOrder();
        entity.setUser(user);
        BigDecimal total = new BigDecimal(123.45f);
        entity.setTotal(total);
        entity.setItems(new ArrayList<>());
        orderRepository.save(entity);

        ResponseEntity<List<UserOrder>> responseEntity = orderController.getOrdersForUser(user.getUsername());
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull().isNotEmpty().hasSize(1);
        assertThat(responseEntity.getBody().get(0).getUser()).isEqualTo(user);
        assertThat(responseEntity.getBody().get(0).getTotal()).isCloseTo(total, Offset.offset(new BigDecimal(0.001f)));

    }

    @Test
    public void testGetOrdersForNonExistingUser() {
//        UserOrder entity = new UserOrder();
//        entity.setUser(user);
//        BigDecimal total = new BigDecimal(123.45f);
//        entity.setTotal(total);
//        entity.setItems(new ArrayList<>());
//        orderRepository.save(entity);

        ResponseEntity<List<UserOrder>> responseEntity = orderController.getOrdersForUser("does not exist");
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();

    }
}