package com.example.demo.model.persistence.repositories;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest extends TestCase {

    @Autowired
    private UserRepository userRepository;
    private User user;

    @Before
    public void setup() {
        user = new User();
        user.setCart(new Cart());
        user.setUsername(UUID.randomUUID().toString());
        user.setPassword("defaultPassword");
        user = userRepository.save(user);
    }

    @Test
    public void testFindByUsername() {
        User byUsername = userRepository.findByUsername(user.getUsername());
        assertThat(byUsername).isNotNull();
        assertThat(byUsername.getId()).isGreaterThan(0L);
        assertThat(byUsername.getUsername()).isEqualTo(user.getUsername());
        assertThat(byUsername.getPassword()).isEqualTo(user.getPassword());
        assertThat(byUsername.getCart()).isNotNull();
        assertThat(byUsername).isEqualTo(user);
    }
}