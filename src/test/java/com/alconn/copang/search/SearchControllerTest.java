package com.alconn.copang.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class SearchControllerTest {

    @Autowired
    MockMvc mvc;

//    @Test
//    void name() throws Exception {
//        this.mvc.perform(
//            get("/search")
//            .param("keyword", "감자")
//        )
//    }
}