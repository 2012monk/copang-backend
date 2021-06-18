package com.alconn.copang.controller;

import com.alconn.copang.security.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
//@AutoConfigureRestDocs
@ExtendWith({SpringExtension.class, RestDocumentationExtension.class})
class HomeControllerTest {

    @MockBean
    CustomUserDetailsService service;

    //    @Autowired
    MockMvc mvc;

    @BeforeEach
    void setUp(WebApplicationContext context,
               RestDocumentationContextProvider provider) {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(provider))
                .alwaysDo(document("{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                .build();
    }

    @Test
    void hello() throws Exception {

        this.mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    void aunauthorizedTest() throws Exception {
        mvc.perform(get("/access"))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }
}