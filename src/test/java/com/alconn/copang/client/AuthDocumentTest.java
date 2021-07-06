package com.alconn.copang.client;

import static com.alconn.copang.ApiDocumentUtils.getAuthHeaderField;
import static com.alconn.copang.ApiDocumentUtils.getDocumentRequest;
import static com.alconn.copang.ApiDocumentUtils.getDocumentResponse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.beneathPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedRequestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.alconn.copang.auth.LoginToken;
import com.alconn.copang.auth.AccessTokenContainer;
import com.alconn.copang.exceptions.InvalidTokenException;
import com.alconn.copang.exceptions.LoginFailedException;
import com.alconn.copang.seller.Seller;
import com.alconn.copang.seller.SellerRepository;
import com.alconn.copang.utils.TestUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.oauth2.sdk.token.AccessToken;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

//@AutoConfigureMockMvc
@AutoConfigureRestDocs
//@WebMvcTest(controllers = AuthController.class )
//@WithMockUser(roles = "GUEST")
@SpringBootTest
@AutoConfigureMockMvc
public class AuthDocumentTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ClientService service;
    //
    @Autowired
    ClientRepo repo;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    EntityManager m;

    @Autowired
    SellerRepository sellerRepository;

    @Autowired
    TestUtils utils;

    @Transactional
    @Test
    void logout() throws Exception {
        Client client = getClient();
        this.repo.save(client);
        LoginToken loginToken = getLoginToken(client);
        AccessTokenContainer container = service.login(loginToken);
        assertNotNull(container);

        System.out.println("container = " + container.getAccess_token());


        this.mvc.perform(
            get("/access2")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + container.getAccess_token())
        )
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(
                document(
                    "auth/with-auth",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    getAuthHeaderField()
                )
            );

        this.mvc.perform(
            get("/api/auth/logout")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + container.getAccess_token())
        ).andExpect(status().isOk())
            .andDo(print())
            .andDo(
                document("auth/logout",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    getAuthHeaderField()
                    )
            );



        this.mvc.perform(
            get("/access2")
        )
            .andExpect(status().isForbidden())
            .andDo(print())
            .andDo(
                document(
                    "auth/auth-failed",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    relaxedResponseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("에러메세지"),
                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("에러코드")
                    )
                )
            );

    }

    @Test
    void getSeller() throws Exception {
        Seller seller = utils.getSeller();

        sellerRepository.save(seller);

        LoginToken loginToken = new LoginToken();
        ReflectionTestUtils.setField(loginToken,"username",seller.getUsername());
        ReflectionTestUtils.setField(loginToken,"password",seller.getPassword());

        AccessTokenContainer container = this.service.login(loginToken);

        this.mvc.perform(
            get("/api/seller/user")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " +container.getAccess_token())
        )
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(
                document(
                    "seller/{method-name}",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    getAuthHeaderField(),
                    relaxedResponseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("에러메세지"),
                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("에러코드"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("조회하신 정보")
                    ),
                    relaxedResponseFields(
                        beneathPath("data").withSubsectionId("data"),
                        fieldWithPath("clientId").type(JsonFieldType.NUMBER).description("유저 식별자"),
                        fieldWithPath("username").type(JsonFieldType.STRING).description("아이디"),
                        fieldWithPath("phone").type(JsonFieldType.STRING).description("휴대전화번호"),
                        fieldWithPath("realName").type(JsonFieldType.STRING).description("이름"),
                        fieldWithPath("role").type(JsonFieldType.STRING).description("유저타입"),
                        fieldWithPath("signInDate").type(JsonFieldType.STRING).description("가입날짜"),
                        fieldWithPath("sellerName").type(JsonFieldType.STRING).description("판매자 명")
//                        fieldWithPath("sellerCode").type(JsonFieldType.NUMBER).description("판매자 코드")
                    )
                )
            );


    }

    @Transactional
    @Test
    void signup() throws Exception {
        Client client = getClient();
        UserForm form = UserForm.builder()
            .username("쿠팡맨")
            .password("비밀번호123!")
            .description("안녕하세요!")
            .phone("010-0030-9090")
            .realName("충성")
            .build();

//        given(service.signupClient(any(UserForm.class))).willReturn(client);

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
                relaxedRequestFields(
                    fieldWithPath("username").type(JsonFieldType.STRING).description("아이디"),
                    fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                    fieldWithPath("realName").type(JsonFieldType.STRING).description("이름"),
                    fieldWithPath("description").type(JsonFieldType.STRING).description("소개")
                        .optional(),
                    fieldWithPath("phone").type(JsonFieldType.STRING).description("휴대전화번호")
                        .optional()
                ),
                relaxedResponseFields(
                    fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메세지"),
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("결과코드"),
                    fieldWithPath("data.clientId").type(JsonFieldType.NUMBER).description("유저 식별자"),
                    fieldWithPath("data.username").type(JsonFieldType.STRING).description("아이디"),
                    fieldWithPath("data.phone").type(JsonFieldType.STRING).description("휴대전화번호"),
                    fieldWithPath("data.realName").type(JsonFieldType.STRING).description("이름"),
                    fieldWithPath("data.role").type(JsonFieldType.STRING).description("유저타입"),
                    fieldWithPath("data.signInDate").type(JsonFieldType.STRING).description("가입날짜")

                )
            )).andDo(print());
    }

    @DisplayName("판매자 등록한다!")
//    @Disabled
    @Test
    void signupSeller() throws Exception {
        UserForm form = UserForm.builder()
            .username("쿠팡맨1412")

            .password("비밀번호123!")
            .description("안녕하세요!")
            .phone("010-0030-9090")
            .realName("충성")
            .build();

//        given(service.signupClient(any(UserForm.class))).willReturn(client);

        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        ResultActions result = this.mvc.perform(
            post("/api/auth/signup/seller")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(mapper.writeValueAsString(form))
        );

        result.andExpect(status().isCreated())
            .andExpect(jsonPath("$.data").exists())
            .andDo(document("auth/signup-seller",
                getDocumentRequest(),
                getDocumentResponse(),
                relaxedRequestFields(
                    fieldWithPath("username").type(JsonFieldType.STRING).description("아이디"),
                    fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                    fieldWithPath("realName").type(JsonFieldType.STRING).description("이름"),
                    fieldWithPath("description").type(JsonFieldType.STRING).description("소개")
                        .optional(),
                    fieldWithPath("phone").type(JsonFieldType.STRING).description("휴대전화번호")
                        .optional()
                ),
                relaxedResponseFields(
                    fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메세지"),
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("결과코드"),
                    fieldWithPath("data.clientId").type(JsonFieldType.NUMBER).description("판매자 식별자"),
                    fieldWithPath("data.username").type(JsonFieldType.STRING).description("아이디"),
                    fieldWithPath("data.phone").type(JsonFieldType.STRING).description("휴대전화번호"),
                    fieldWithPath("data.realName").type(JsonFieldType.STRING).description("이름"),
                    fieldWithPath("data.role").type(JsonFieldType.STRING).description("유저타입")
//                    fieldWithPath("data.signInDate").type(JsonFieldType.STRING).description("가입날짜")

                )
            )).andDo(print());
    }

    @Transactional
    @Test
    void delete() throws Exception {
        Client client = getClient();

        this.repo.save(client);
        LoginToken token = getLoginToken(client);

        AccessTokenContainer container = this.service.login(token);

        this.mvc.perform(
            MockMvcRequestBuilders.delete("/api/user")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + container.getAccess_token())
        )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(
                document(
                    "auth/delete",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    getAuthHeaderField(),
                    relaxedResponseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메세지"),
                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("결과코드")
                    )
                )
            );


    }

    private Client getClient() {
        return Client.builder()
            .username("쿠팡맨")
            .password("비밀번호123!")
            .description("안녕하세요!")
            .phone("010-0030-9090")
            .realName("충성")
            .role(Role.CLIENT)
            .build();
    }

    @Transactional
    @Test
    void login() throws Exception {

        Client client = Client.builder()
            .username("쿠팡맨13")
            .password("비밀번호123!")
            .description("안녕하세요!")
            .phone("010-0030-9090")
            .realName("충성")
            .role(Role.CLIENT)
            .build();
        repo.save(client);

        LoginToken token = getLoginToken(client);

//        given(repo.save(any(Client.class))).willReturn(client);

        System.out.println("client.getId() = " + client.getClientId());
        AccessTokenContainer tokenContainer = AccessTokenContainer.builder().build();
//        when(repo.save(client)).thenReturn(client);
        System.out
            .println("tokenContainer.getAccess_token() = " + tokenContainer.getAccess_token());
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
                    fieldWithPath("data.access_token").type(JsonFieldType.STRING)
                        .description("AcessToken 인증토큰"),
                    fieldWithPath("data.username").type(JsonFieldType.STRING).description("아이디"),
                    fieldWithPath("data.role").type(JsonFieldType.STRING).description("유저타입")
                )
            ))
            .andDo(print());
    }

    private LoginToken getLoginToken(Client client) {
        LoginToken token = new LoginToken();
        ReflectionTestUtils.setField(token, "username", client.getUsername());
        ReflectionTestUtils.setField(token, "password", client.getPassword());
        return token;
    }
}
