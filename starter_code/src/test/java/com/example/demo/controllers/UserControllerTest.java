package com.example.demo.controllers;

import com.example.demo.model.persistence.User;
import com.example.demo.model.requests.CreateUserRequest;
import junit.framework.TestCase;
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
public class UserControllerTest extends TestCase {

    @Autowired
    private UserController userController;

    @Test
    public void testCreateUser() {
        CreateUserRequest request = given_aCreateUserRequest();
        ResponseEntity<User> userResponse = when_aCreateUserResponseIsSent(request);
        then_StatusOkAndTheNewlyCreatedUserWithAHashedPasswordIsReturned(request, userResponse);
    }

    @Test
    public void testFindById() {
        User user = given_aDefaultUser();
        ResponseEntity<User> userResponseEntity = when_aFindByIdRequestIsSent(user.getId());
        then_StatusOkAndUserIsReturned(userResponseEntity, user);
    }

    @Test
    public void testFindByUserName() {
        User user = given_aDefaultUser();
        ResponseEntity<User> userResponseEntity = when_aFindByNameRequestIsSent(user.getUsername());
        then_StatusOkAndUserIsReturned(userResponseEntity, user);
    }

    @Test
    public void testCreateUserExistingName() {
        User user = given_aDefaultUser();
        CreateUserRequest faulty = given_aCrateUserRequestWithUsername(user.getUsername());
        ResponseEntity<User> response = when_aCreateUserResponseIsSent(faulty);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNull();
    }

    private CreateUserRequest given_aCrateUserRequestWithUsername(String username) {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername(username);
        request.setPassword("foobarbar");
        request.setConfirmPassword("foobarbar");
        return request;
    }

    private CreateUserRequest given_aCreateUserRequest() {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername(UUID.randomUUID().toString());
        request.setPassword("testPassword");
        request.setConfirmPassword("testPassword");
        return request;
    }

    private User given_aDefaultUser() {
        return userController.createUser(given_aCreateUserRequest()).getBody();
    }

    private ResponseEntity<User> when_aFindByIdRequestIsSent(long id) {
        return userController.findById(id);
    }

    private ResponseEntity<User> when_aFindByNameRequestIsSent(String username) {
        return userController.findByUserName(username);
    }

    private ResponseEntity<User> when_aCreateUserResponseIsSent(CreateUserRequest request) {
        ResponseEntity<User> userResponse = userController.createUser(request);
        return userResponse;
    }

    private void then_StatusOkAndTheNewlyCreatedUserWithAHashedPasswordIsReturned(CreateUserRequest request, ResponseEntity<User> userResponse) {
        String username = request.getUsername();
        String password = request.getPassword();
        assertStatusOKAndBodyNotNull(userResponse);
        assertThat(userResponse.getBody().getUsername()).isEqualTo(username);
        assertThat(userResponse.getBody().getPassword()).isNotEqualTo(password);
    }

    private void then_StatusOkAndUserIsReturned(ResponseEntity<User> userResponseEntity, User user) {
        assertStatusOKAndBodyNotNull(userResponseEntity);
        assertThat(userResponseEntity.getBody()).isEqualTo(user);
    }

    private void assertStatusOKAndBodyNotNull(ResponseEntity<User> userResponse) {
        assertThat(userResponse).isNotNull();
        assertThat(userResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(userResponse.getBody()).isNotNull();
    }
}