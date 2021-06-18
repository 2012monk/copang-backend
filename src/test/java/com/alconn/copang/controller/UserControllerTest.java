package com.alconn.copang.controller;

import com.alconn.copang.client.Client;
import com.alconn.copang.client.ClientRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class UserControllerTest {

    @Autowired
    MockMvc mvc;

    @Mock
    ClientRepo repo;

    @Autowired
    ObjectMapper mapper;

    @Transactional
    @Test
    void getUser() throws Exception {

        Client client = Client.builder()
                .username("test")
                .password("1234")
                .build();

        given(this.repo.save(client)).willReturn(client);


        log.info(String.valueOf(client.getId()));

//
        this.mvc.perform(
                get("/api/user/list")
        ).andExpect(jsonPath("$.data").exists())
                .andDo(print());


    }


    @Test
    void createClient() throws Exception {
        Client client = Client.builder()
                .username("test")
                .password("1234")
                .build();

        given(repo.save(client)).willReturn(client);


        this.mvc.perform(post("/api/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(client)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.username").value(client.getUsername()))
                .andDo(print());
    }
}