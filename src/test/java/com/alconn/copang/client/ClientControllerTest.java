package com.alconn.copang.client;

import com.alconn.copang.ApiDocumentUtils;
import com.alconn.copang.auth.LoginToken;
import com.alconn.copang.common.AccessTokenContainer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;

import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
//@ExtendWith(RestDocumentationExtension.class)
class ClientControllerTest {

    @Autowired
    private MockMvc mvc;


    @Autowired
    private ModelMapper mapper;

    @Autowired
    ClientRepo repo;

    @Autowired
    ClientService service;

    @Autowired
    ObjectMapper objectMapper;


//    @BeforeEach
    void setUp() {
        Client client = Client.builder()
                .username("test")
                .password("1234")
                .build();

//        given(this.repo.save(client)).willReturn(client);
        Random random = new Random();
        List<Client> list = new ArrayList<>();
        for (int i=0;i<11;i++) {
            list.add(Client.builder()
                    .username("테스트 유저입니다!!" + random.nextInt())
                    .description("안녕하세요!")
                    .phone("010-0030-9090")
                    .realName("길동홍길동")
                    .role(Role.CLIENT)
                    .password(String.valueOf(random.nextInt())).build());
        }
    }

    @DisplayName("유저 리스트 가져오기")
    @Transactional
    @Test
    void getUserList() throws Exception {
        Client client = Client.builder()
                .username("coppang143")
                .password("비밀번호123!")
                .role(Role.CLIENT)
                .phone("010-0030-9090")
                .description("안녕하세요!")
                .role(Role.CLIENT)
                .realName("길동홍길동")
                .build();
        this.repo.save(client);

        LoginToken token = new LoginToken();
        ReflectionTestUtils.setField(token, "username", client.getUsername());
        ReflectionTestUtils.setField(token, "password", client.getPassword());
        AccessTokenContainer container = service.login(token);

        Random random = new Random();
        List<Client> list = new ArrayList<>();
        for (int i=0;i<5;i++) {
            list.add(Client.builder()
                    .username("테스트 유저입니다!!" + random.nextInt())
                    .description("안녕하세요!")
                    .phone("010-0030-9090")
                    .description("안녕하세요!")
                    .role(Role.CLIENT)
                    .realName("길동홍길동")
//                    .role(Role.CLIENT)
                    .password(String.valueOf(random.nextInt())).build());
        }

        this.repo.saveAll(list);

//        this.repo.save(client);
        FieldDescriptor[] fieldDescriptors = new FieldDescriptor[]{

        };
        this.mvc.perform(
                get("/api/user/list")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + container.getAccess_token())
        ).andExpect(jsonPath("$.data").exists())
                .andDo(document("client/{method-name}",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer scheme Access Token 인증 토큰")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메세지"),
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("결과코드"),
                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("회원 리스트"),
                                fieldWithPath("data.[].id").type(JsonFieldType.NUMBER).description("고유번호"),
                                fieldWithPath("data.[].username").type(JsonFieldType.STRING).description("아이디"),
                                fieldWithPath("data.[].description").type(JsonFieldType.STRING).description("소개"),
                                fieldWithPath("data.[].phone").type(JsonFieldType.STRING).description("휴대전화번호"),
                                fieldWithPath("data.[].realName").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("data.[].role").type(JsonFieldType.STRING).description("유저타입"),
                                fieldWithPath("data.[].signInDate").type(JsonFieldType.STRING).description("가입날짜")
//                                fieldWithPath("data[]").ignored()
                        )

                    ))
                .andDo(print());


    }

    @Transactional
    @DisplayName("유저 하나 가져오기")
    @Test
    void getOneUser() throws Exception {
        Client client = Client.builder()
                .username("coppang143")
                .password("비밀번호123!")
                .description("안녕하세요!")
                .phone("010-0030-9090")
                .realName("길동홍길동")
                .role(Role.CLIENT)
                .build();
        this.repo.save(client);

        LoginToken token = new LoginToken();
        ReflectionTestUtils.setField(token, "username", client.getUsername());
        ReflectionTestUtils.setField(token, "password", client.getPassword());
        AccessTokenContainer container = service.login(token);

        this.mvc.perform(
                RestDocumentationRequestBuilders.
                get("/api/user/{id}", client.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer "+ container.getAccess_token())
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$..username").value(client.getUsername()))
//                .andExpect(jsonPath("$..password").value(client.getPassword()))
                .andDo(document("client/{method-name}",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        pathParameters(
                                parameterWithName("id").description("아이디")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer scheme Access Token 인증 토큰")
                        ),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메세지"),
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("결과코드"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("고유번호"),
                                fieldWithPath("data.username").type(JsonFieldType.STRING).description("아이디"),
                                fieldWithPath("data.description").type(JsonFieldType.STRING).description("소개"),
                                fieldWithPath("data.phone").type(JsonFieldType.STRING).description("휴대전화번호"),
                                fieldWithPath("data.realName").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("data.role").type(JsonFieldType.STRING).description("유저타입"),
                                fieldWithPath("data.signInDate").type(JsonFieldType.STRING).description("가입날짜")
                        )
                ))
                .andDo(print());
    }

    @Transactional
    @Test
    void update() throws Exception {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        Client client = Client.builder()
                .username("coppang143")
                .password("비밀번호123!")
                .description("안녕하세요!")
                .phone("010-0030-9090")
                .realName("길동홍길동")
                .role(Role.CLIENT)
                .build();

        this.repo.save(client);
        LoginToken token = new LoginToken();
        ReflectionTestUtils.setField(token, "username", client.getUsername());
        ReflectionTestUtils.setField(token, "password", client.getPassword());
        AccessTokenContainer container = service.login(token);
        UserForm form = mapper.map(client, UserForm.class);
//        UserForm form = new UserForm();
        ReflectionTestUtils.setField(form, "realName", "이름바꿨습니다");


        ResultActions result = this.mvc.perform(
                RestDocumentationRequestBuilders.
                put("/api/user/{id}", client.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(form))
                .characterEncoding("utf-8")
                .header(HttpHeaders.AUTHORIZATION, "Bearer "+ container.getAccess_token())
        );
                result.andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andDo(document("client/{method-name}",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        pathParameters(
                                parameterWithName("id").description("아이디")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer scheme Access Token 인증 토큰")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메세지"),
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("결과코드"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("고유번호"),
                                fieldWithPath("data.username").type(JsonFieldType.STRING).description("아이디"),
                                fieldWithPath("data.description").type(JsonFieldType.STRING).description("소개"),
                                fieldWithPath("data.phone").type(JsonFieldType.STRING).description("휴대전화번호"),
                                fieldWithPath("data.realName").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("data.role").type(JsonFieldType.STRING).description("유저타입")
//                                fieldWithPath("data.signInDate").type(JsonFieldType.STRING).description("가입날짜")
                        )
                ))
                .andDo(print());
    }


}