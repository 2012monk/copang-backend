package com.alconn.copang.client;

import com.alconn.copang.ApiDocumentUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
class ClientControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mvc;



    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentationExtension) {
        this.mvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(restDocumentationExtension).operationPreprocessors().withRequestDefaults(
                        Preprocessors.removeMatchingHeaders("Vary")
                ))
                .alwaysDo(document(
                        "client/{method-name}",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse()
                ))
                .build();
    }

    @Autowired
    ClientRepo repo;


    @DisplayName("유저 리스트 가져오기")
    @Transactional
    @Test
    void getUser() throws Exception {
        Client client = Client.builder()
                .username("test")
                .password("1234")
                .build();

//        given(this.repo.save(client)).willReturn(client);
        Random random = new Random();
        List<Client> list = new ArrayList<>();
        for (int i=0;i<15;i++) {
            list.add(Client.builder().username("test" + random.nextInt())
                    .password(String.valueOf(random.nextInt())).build());
        }
//        given(this.repo.save(client)).willReturn(client);

        this.repo.saveAll(list);

//        this.repo.save(client);

        log.info(String.valueOf(client.getId()));

//
        this.mvc.perform(
                get("/api/user/list")
        ).andExpect(jsonPath("$.data").exists())
                .andDo(print());


    }

    @Transactional
    @DisplayName("유저 하나 가져오기")
    @Test
    void getOneUser() throws Exception {
        Client client = Client.builder()
                .username("test23")
                .password("1234")
                .build();

        this.repo.save(client);

        this.mvc.perform(
                RestDocumentationRequestBuilders.
                get("/api/user/{id}", client.getId())
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$..username").value(client.getUsername()))
                .andExpect(jsonPath("$..password").value(client.getPassword()))
//                .andDo(document("client/{method-name}",
//                        ApiDocumentUtils.getDocumentRequest(),
//                        ApiDocumentUtils.getDocumentResponse(),
//                        pathParameters(
//                                parameterWithName(":id").description("조회할 유저 아이디")
//                        )))
                .andDo(print());


    }

}