package com.example.demo.model.persistence.repositories;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
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

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CartRepositoryTest extends TestCase {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    private User user;
    private Cart cart;
    private Item item;

    @Before
    public void setUp() throws Exception {
        user = new User();
        cart = new Cart();
        item = new Item();
        item.setId(1L);
        item.setPrice(new BigDecimal(2.99f));
        cart.addItem(item);
        user.setCart(cart);
        user.setUsername("user");
        user.setPassword("pass");
        user = userRepository.save(user);
    }

    @Test
    @Transactional
    public void testFindByUser() {
        Cart byUser = cartRepository.findByUser(user);
        assertThat(byUser).isNotNull();
        assertThat(byUser.getItems()).isNotNull().isNotEmpty().hasSize(1);
        assertThat(byUser.getItems()).containsExactlyInAnyOrderElementsOf(Arrays.asList(item));
    }
}