package com.example.demo.model.persistence.repositories;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderRepositoryTest extends TestCase {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private User user;
    private Cart cart;
    private Item item;
    private UserOrder order;

    @Before
    public void setUp() throws Exception {
        user = new User();
        cart = new Cart();
        item = getItem();
        cart.addItem(item);
        user.setCart(cart);
        user.setUsername("user");
        user.setPassword("pass");
        user = userRepository.save(user);
    }

    private Item getItem() {
        Item item = new Item();
        item.setId(1L);
        item.setPrice(new BigDecimal(2.99f));
        return item;
    }

    @Test
    @Transactional
    public void testFindByUser() {

        order = new UserOrder();
        Item orderItem = itemRepository.findById(1L).orElseThrow(() -> new RuntimeException("Item not found, shouldn't happen!"));
        order.setItems(Arrays.asList(orderItem));
        order.setTotal(orderItem.getPrice());
        order.setUser(user);
        order = orderRepository.save(order);

        List<UserOrder> byUser = orderRepository.findByUser(user);
        assertThat(byUser).isNotNull().isNotEmpty().hasSize(1);
        assertThat(byUser.get(0).getUser()).isEqualTo(user);
        assertThat(byUser.get(0).getTotal()).isEqualTo(order.getTotal());
        assertThat(byUser.get(0).getItems()).containsExactlyInAnyOrderElementsOf(cart.getItems());
        assertThat(byUser.get(0)).isEqualTo(order);
    }
}