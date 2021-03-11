package com.example.demo;

import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import junit.framework.TestCase;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractBaseTestCase extends TestCase {

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected CartRepository cartRepository;

    @Autowired
    protected ItemRepository itemRepository;

    @Autowired
    protected OrderRepository orderRepository;

    public void setup() {
        orderRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        cartRepository.deleteAllInBatch();
    }
}
