package com.alconn.copang.controller;

import com.alconn.copang.client.Client;
import com.alconn.copang.store.Store;
import com.alconn.copang.store.StoreRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@ExtendWith(SpringExtension.class)
class StoreControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    StoreRepo repo;

    @Autowired
    ObjectMapper mapper;


    Client client = Client.builder().username("test").password("1234").description("desc").build();
    @Test
    void createStore() throws Exception {
        Store store =
            Store.builder()
                    .storeName("store1")
                    .description("가게입니다")
                    .build();

        repo.save(store);
//        given(repo.save(store)).willReturn(store);

        this.mvc.perform(
                post("/api/stores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(store))
        ).andExpect(jsonPath("$.data").exists())
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    void getAllStores() throws Exception {
        Store store =
                Store.builder()
                        .storeName("store1")
                        .description("가게입니다")
                        .build();

        repo.save(store);

        this.mvc.perform(
                get("/api/stores/list")
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data[0].name").value(store.getStoreName()))
                .andDo(print());
    }

    @Test
    void getStoreById() throws Exception {
        Store store =
                Store.builder()
                        .storeName("store1")
                        .description("가게입니다")
                        .build();

        repo.save(store);

        this.mvc.perform(
                get("/api/stores/" + store.getId().toString())
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.name").value(store.getStoreName()))
                .andExpect(jsonPath("$.data.desc").value(store.getDescription()))
                .andDo(print());
    }
}