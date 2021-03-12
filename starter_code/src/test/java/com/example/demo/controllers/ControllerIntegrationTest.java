package com.example.demo.controllers;

import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class ControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<CreateUserRequest> jsonCUR;

    @Autowired
    private JacksonTester<LoginRequest> jsonLR;

    @Test
    public void test() throws Exception {
        CreateUserRequest cur = new CreateUserRequest();
        String uuid = UUID.randomUUID().toString();
        cur.setUsername(uuid);
        cur.setPassword(uuid);
        cur.setConfirmPassword(uuid);
        mvc.perform(post(new URI("/api/user/create")).content(jsonCUR.write(cur).getJson())
                .contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        mvc.perform(get(new URI("/api/user/id/1")).accept(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isUnauthorized());

        LoginRequest lr = new LoginRequest(uuid, uuid);
        mvc.perform(post(new URI("/login")).content(jsonLR.write(lr).getJson()).contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk());
    }

    class LoginRequest {
        private String username;
        private String password;

        public LoginRequest(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
