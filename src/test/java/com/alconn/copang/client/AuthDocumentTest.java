package com.alconn.copang.client;

import com.alconn.copang.auth.AuthController;
import com.alconn.copang.auth.LoginToken;
import com.alconn.copang.common.AccessTokenContainer;
import com.alconn.copang.config.SecurityConfig;
import com.alconn.copang.exceptions.InvalidTokenException;
import com.alconn.copang.exceptions.LoginFailedException;
import com.alconn.copang.security.provider.JwtAuthenticationProvider;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static com.alconn.copang.ApiDocumentUtils.getDocumentRequest;
import static com.alconn.copang.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@AutoConfigureMockMvc
@AutoConfigureRestDocs
//@WebMvcTest(controllers = AuthController.class )
//@WithMockUser(roles = "GUEST")
@SpringBootTest
@AutoConfigureMockMvc
public class AuthDocumentTest {

    @Autowired
    MockMvc mvc;

////    @MockBean
//    @Mock
//    AuthController controller;
//
    @Mock
    ClientService service;
//
    @Autowired
    ClientRepo repo;

    @Autowired
    ObjectMapper mapper;

    @Transactional
    @Test
    void signup() throws Exception {
        Client client = Client.builder()
                .username("쿠팡맨")
                .password("비밀번호123!")
                .description("안녕하세요!")
                .mobile("010-0030-9090")
                .realName("충성")
                .build();
        UserForm form = UserForm.builder()
                .username("쿠팡맨")
                .password("비밀번호123!")
                .description("안녕하세요!")
                .mobile("010-0030-9090")
                .realName("충성")
                .build();

        given(service.signupClient(any(UserForm.class))).willReturn(client);

        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        ResultActions result = this.mvc.perform(
                post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(mapper.writeValueAsString(form))
        );

        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.data").exists())
                .andDo(document("auth/signup-client",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("username").type(JsonFieldType.STRING).description("아이디"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                                fieldWithPath("realName").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("description").type(JsonFieldType.STRING).description("소개").optional(),
                                fieldWithPath("mobile").type(JsonFieldType.STRING).description("휴대전화번호").optional()
                        ),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메세지"),
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("결과코드"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("고유번호"),
                                fieldWithPath("data.username").type(JsonFieldType.STRING).description("아이디"),
                                fieldWithPath("data.description").type(JsonFieldType.STRING).description("소개"),
                                fieldWithPath("data.mobile").type(JsonFieldType.STRING).description("휴대전화번호"),
                                fieldWithPath("data.realName").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("data.role").type(JsonFieldType.STRING).description("유저타입"),
                                fieldWithPath("data.signInDate").type(JsonFieldType.STRING).description("가입날짜")

                        )
                )).andDo(print());
    }

    @Transactional
    @Test
    void login() throws Exception {

        Client client = Client.builder()
                .username("쿠팡맨")
                .password("비밀번호123!")
                .description("안녕하세요!")
                .mobile("010-0030-9090")
                .realName("충성")
                .role(Role.CLIENT)
                .build();
        repo.save(client);

        LoginToken token = new LoginToken();
        ReflectionTestUtils.setField(token, "username", client.getUsername());
        ReflectionTestUtils.setField(token, "password", client.getPassword());

//        given(repo.save(any(Client.class))).willReturn(client);

        System.out.println("client.getId() = " + client.getId());
        AccessTokenContainer tokenContainer = AccessTokenContainer.builder().build();
//        when(repo.save(client)).thenReturn(client);
        System.out.println("tokenContainer.getAccess_token() = " + tokenContainer.getAccess_token());
        ResultActions result = this.mvc.perform(
                post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(token))
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andDo(document("auth/login",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("username").type(JsonFieldType.STRING).description("아이디"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메세지"),
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("결과코드"),
                                fieldWithPath("data.access_token").type(JsonFieldType.STRING).description("AcessToken 인증토큰"),
                                fieldWithPath("data.username").type(JsonFieldType.STRING).description("아이디"),
                                fieldWithPath("data.role").type(JsonFieldType.STRING).description("유저타입")

                        )
                        ))
                .andDo(print());
    }
}
