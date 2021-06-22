package com.alconn.copang.controller;

import com.alconn.copang.auth.LoginToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@Disabled
@SpringBootTest
@AutoConfigureMockMvc
class ValidateControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;



    @Test
    void name() throws Exception {
        LoginToken token = new LoginToken();
//        ReflectionTestUtils.setField(token, "username", "name");

        mvc.perform(post("/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(mapper.writeValueAsString(token)
        ))

                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}