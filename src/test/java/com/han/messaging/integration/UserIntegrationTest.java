package com.han.messaging.integration;

import com.han.messaging.dao.UserDAO;
import com.han.messaging.dao.UserValidationCodeDAO;
import com.han.messaging.model.User;
import com.han.messaging.model.UserValidationCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private UserValidationCodeDAO userValidationCodeDAO;

    @BeforeEach
    public void prepare() {
        this.userDAO.deleteAll();
        this.userValidationCodeDAO.deleteAll();
    }

    @Test
    public void testRegister_validInput_registerSuccessful() throws Exception {

        // input preparation -> invoke/perform -> validation
        var requestBody = "{\n" +
                "\t\"username\": \"sdfdsfsdfsf\",\n" +
                "\t\"nickname\": \"nickname\",\n" +
                "\t\"address\": \"canada\",\n" +
                "\t\"email\": \"sdfdsfdsfds@gmail.com\",\n" +
                "\t\"gender\": \"MALE\",\n" +
                "\t\"password\": \"xxxxxxxxxxxxxxxxxxc\",\n" +
                "\t\"repeatPassword\": \"xxxxxxxxxxxxxxxxxxc\"\n" +
                "}";
        this.mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.message").value("Successful"));


        User user = this.userDAO.selectByUsername("sdfdsfsdfsf");
        assertNotNull(user);
        assertEquals("nickname", user.getNickname());
        assertEquals("sdfdsfdsfds@gmail.com", user.getEmail());
        //...

        UserValidationCode userValidationCode = this.userValidationCodeDAO.findLatestByUserId(user.getId());
        assertNotNull(userValidationCode);
        assertEquals(user.getId(), userValidationCode.getUserId());
        assertEquals(6, userValidationCode.getValidationCode().length());
    }

    @Test
    public void testRegister_mismatchedPasswords_respondPasswordsNotMatched() throws Exception {
        // input preparation -> invoke/perform -> validation
        var requestBody = "{\n" +
                "\t\"username\": \"sdfdsfsdfsf\",\n" +
                "\t\"nickname\": \"nickname\",\n" +
                "\t\"address\": \"canada\",\n" +
                "\t\"email\": \"sdfdsfdsfds@gmail.com\",\n" +
                "\t\"gender\": \"MALE\",\n" +
                "\t\"password\": \"xxxxxxxxxxxxxxcxcxcxcxcxcxc\",\n" +
                "\t\"repeatPassword\": \"xxxxxxxxxxxxxxxxxxc\"\n" +
                "}";
        this.mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code").value(1001))
                .andExpect(jsonPath("$.message").value("Passwords are not matched"));
    }

    @Test
    public void testActivate_validInput_activateSuccessful() throws Exception {
        var requestBody = "{\n" +
                "\t\"username\": \"sdfdsfsdfsf\",\n" +
                "\t\"nickname\": \"nickname\",\n" +
                "\t\"address\": \"canada\",\n" +
                "\t\"email\": \"sdfdsfdsfds@gmail.com\",\n" +
                "\t\"gender\": \"MALE\",\n" +
                "\t\"password\": \"xxxxxxxxxxxxxxxxxxc\",\n" +
                "\t\"repeatPassword\": \"xxxxxxxxxxxxxxxxxxc\"\n" +
                "}";
        this.mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        User user = this.userDAO.selectByUsername("sdfdsfsdfsf");
        UserValidationCode userValidationCode = this.userValidationCodeDAO.findLatestByUserId(user.getId());

        requestBody = "{\n" +
                "\t\"identification\": \"sdfdsfsdfsf\",\n" +
                "\t\"password\": \"xxxxxxxxxxxxxxxxxxc\",\n" +
                "\t\"validationCode\":\"" + userValidationCode.getValidationCode() + "\"\n" +
                "}";

        this.mockMvc.perform(post("/users/activate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.message").value("Successful"));

        user = this.userDAO.selectByUsername("sdfdsfsdfsf");
        assertTrue(user.getValid());

        userValidationCode = this.userValidationCodeDAO.findLatestByUserId(user.getId());
        assertNull(userValidationCode);
    }
}
