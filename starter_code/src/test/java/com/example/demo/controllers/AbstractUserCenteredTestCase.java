package com.example.demo.controllers;

import com.example.demo.model.persistence.User;
import com.example.demo.model.requests.CreateUserRequest;
import junit.framework.TestCase;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public abstract class AbstractUserCenteredTestCase extends TestCase {

    @Autowired
    protected UserController userController;
    protected User user;

    @Before
    public void setup() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        String uuid = UUID.randomUUID().toString();
        createUserRequest.setUsername(uuid);
        createUserRequest.setPassword(uuid);
        createUserRequest.setConfirmPassword(uuid);
        ResponseEntity<User> userResponseEntity = userController.createUser(createUserRequest);
        user = userResponseEntity.getBody();
    }
}
