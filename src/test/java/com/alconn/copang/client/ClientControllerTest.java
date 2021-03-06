package com.alconn.copang.client;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.alconn.copang.ApiDocumentUtils;
import com.alconn.copang.auth.AccessTokenContainer;
import com.alconn.copang.auth.LoginToken;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@Slf4j
@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
class ClientControllerTest {

    @Autowired
    ClientRepo repo;
    @Autowired
    ClientService service;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    ClientMapper mapper;
    @Autowired
    private MockMvc mvc;

    //    @BeforeEach
    void setUp() {
        Client client = Client.builder()
            .username("test")
            .password("1234")
            .build();

//        given(this.repo.save(client)).willReturn(client);
        Random random = new Random();
        List<Client> list = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            list.add(Client.builder()
                .username("????????? ???????????????!!" + random.nextInt())
                .description("???????????????!")
                .phone("010-0030-9090")
                .realName("???????????????")
                .role(Role.CLIENT)
                .password(String.valueOf(random.nextInt())).build());
        }
    }

    @DisplayName("?????? ????????? ????????????")
    @Transactional
    @Test
    void getUserList() throws Exception {
        Client client = Client.builder()
            .username("coppang143")
            .password("????????????123!")
            .role(Role.CLIENT)
            .phone("010-0030-9090")
            .description("???????????????!")
            .role(Role.CLIENT)
            .realName("???????????????")
            .build();
        this.repo.save(client);

        LoginToken token = new LoginToken();
        ReflectionTestUtils.setField(token, "username", client.getUsername());
        ReflectionTestUtils.setField(token, "password", client.getPassword());
        AccessTokenContainer container = service.login(token);

        Random random = new Random();
        List<Client> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(Client.builder()
                .username("????????? ???????????????!!" + random.nextInt())
                .description("???????????????!")
                .phone("010-0030-9090")
                .description("???????????????!")
                .role(Role.CLIENT)
                .realName("???????????????")
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
                    headerWithName(HttpHeaders.AUTHORIZATION)
                        .description("Bearer scheme Access Token ?????? ??????")
                ),
                relaxedResponseFields(
                    fieldWithPath("message").type(JsonFieldType.STRING).description("?????? ?????????"),
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("????????????"),
                    fieldWithPath("data").type(JsonFieldType.ARRAY).description("?????? ?????????"),
                    fieldWithPath("data.[].clientId").type(JsonFieldType.NUMBER)
                        .description("?????? ??????"),
                    fieldWithPath("data.[].username").type(JsonFieldType.STRING).description("?????????"),
                    fieldWithPath("data.[].description").type(JsonFieldType.STRING)
                        .description("??????"),
                    fieldWithPath("data.[].phone").type(JsonFieldType.STRING).description("??????????????????"),
                    fieldWithPath("data.[].realName").type(JsonFieldType.STRING).description("??????"),
                    fieldWithPath("data.[].role").type(JsonFieldType.STRING).description("????????????"),
                    fieldWithPath("data.[].signInDate").type(JsonFieldType.STRING)
                        .description("????????????")
//                                fieldWithPath("data[]").ignored()
                )

            ))
            .andDo(print());


    }

    @Transactional
    @DisplayName("?????? ?????? ????????????")
    @Test
    void getOneUser() throws Exception {
        Client client = Client.builder()
            .username("coppang143")
            .password("????????????123!")
            .description("???????????????!")
            .phone("010-0030-9090")
            .realName("???????????????")
            .role(Role.CLIENT)
            .build();
        this.repo.save(client);

        LoginToken token = new LoginToken();
        ReflectionTestUtils.setField(token, "username", client.getUsername());
        ReflectionTestUtils.setField(token, "password", client.getPassword());
        AccessTokenContainer container = service.login(token);

        this.mvc.perform(
            RestDocumentationRequestBuilders.
                get("/api/user")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + container.getAccess_token())
        ).andExpect(status().isOk())
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$..username").value(client.getUsername()))
//                .andExpect(jsonPath("$..password").value(client.getPassword()))
            .andDo(document("client/{method-name}",
                ApiDocumentUtils.getDocumentRequest(),
                ApiDocumentUtils.getDocumentResponse(),
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION)
                        .description("Bearer scheme Access Token ?????? ??????")
                ),
                relaxedResponseFields(
                    fieldWithPath("message").type(JsonFieldType.STRING).description("?????? ?????????"),
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("????????????"),
                    fieldWithPath("data.clientId").type(JsonFieldType.NUMBER).description("????????????"),
                    fieldWithPath("data.username").type(JsonFieldType.STRING).description("?????????"),
                    fieldWithPath("data.description").type(JsonFieldType.STRING).description("??????"),
                    fieldWithPath("data.phone").type(JsonFieldType.STRING).description("??????????????????"),
                    fieldWithPath("data.realName").type(JsonFieldType.STRING).description("??????"),
                    fieldWithPath("data.role").type(JsonFieldType.STRING).description("????????????"),
                    fieldWithPath("data.signInDate").type(JsonFieldType.STRING).description("????????????")
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
            .password("????????????123!")
            .description("???????????????!")
            .phone("010-0030-9090")
            .realName("???????????????")
            .role(Role.CLIENT)
            .build();

        this.repo.save(client);
        LoginToken token = new LoginToken();
        ReflectionTestUtils.setField(token, "username", client.getUsername());
        ReflectionTestUtils.setField(token, "password", client.getPassword());
        AccessTokenContainer container = service.login(token);
        UserForm form = mapper.c(client);

//        UserForm form = new UserForm();
        ReflectionTestUtils.setField(form, "realName", "?????????????????????");

        ResultActions result = this.mvc.perform(
            RestDocumentationRequestBuilders.
                put("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(form))
                .characterEncoding("utf-8")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + container.getAccess_token())
        );
        result.andExpect(status().isOk())
            .andExpect(jsonPath("$.data").exists())
            .andDo(document("client/{method-name}",
                ApiDocumentUtils.getDocumentRequest(),
                ApiDocumentUtils.getDocumentResponse(),
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION)
                        .description("Bearer scheme Access Token ?????? ??????")
                ),
                relaxedResponseFields(
                    fieldWithPath("message").type(JsonFieldType.STRING).description("?????? ?????????"),
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("????????????"),
                    fieldWithPath("data.clientId").type(JsonFieldType.NUMBER).description("?????? ??????"),
                    fieldWithPath("data.username").type(JsonFieldType.STRING).description("?????????"),
                    fieldWithPath("data.description").type(JsonFieldType.STRING).description("??????"),
                    fieldWithPath("data.phone").type(JsonFieldType.STRING).description("??????????????????"),
                    fieldWithPath("data.realName").type(JsonFieldType.STRING).description("??????"),
                    fieldWithPath("data.role").type(JsonFieldType.STRING).description("????????????")
//                                fieldWithPath("data.signInDate").type(JsonFieldType.STRING).description("????????????")
                )
            ))
            .andDo(print());
    }


}