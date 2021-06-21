package com.alconn.copang.client;

import com.alconn.copang.ApiDocumentUtils;
import com.alconn.copang.auth.AuthController;
import com.alconn.copang.common.AccessTokenContainer;
import com.alconn.copang.common.LoginToken;
import com.alconn.copang.config.SecurityConfig;
import com.alconn.copang.exceptions.InvalidTokenException;
import com.alconn.copang.exceptions.LoginFailedException;
import com.alconn.copang.security.CustomLogoutHandler;
import com.alconn.copang.security.CustomUserDetailsService;
import com.alconn.copang.security.JwtValidateFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.Filter;
import javax.transaction.Transactional;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@SpringBootTest
//@AutoConfigureMockMvc
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
class AuthControllerTest {

    final String domain = "/api/auth/";
    final Client client = Client.builder()
            .username("test")
            .password("1234")
            .role(Role.CLIENT)
            .build();
//    @Autowired
    MockMvc mvc;

    @Autowired
    SecurityConfig config;

    @Autowired
    ClientRepo repo;

    @Autowired
    ClientService service;
    @Autowired
    ObjectMapper mapper;

    @Autowired
    WebApplicationContext context;

    @Autowired
    CustomUserDetailsService userDetailsService;

    @Autowired
    JwtValidateFilter filter;
    @Autowired
    @Value("blackList")
    Set<String> blackList;

    @Autowired
    CustomLogoutHandler handler;



    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        this.mvc = MockMvcBuilders
//                .standaloneSetup(AuthController.class)
                .webAppContextSetup(this.context)
//                .addFilters(new JwtValidateFilter(userDetailsService, blackList))
                .addFilters(filter)
                .addFilters(new CharacterEncodingFilter("UTF-8"))
                .apply(documentationConfiguration(restDocumentation))
                .alwaysDo(document("Auth/{method-name}", ApiDocumentUtils.getDocumentRequest(), ApiDocumentUtils.getDocumentResponse()))
                .build();
    }





    @Test
    void createClient() throws Exception {
        Client client = Client.builder()
                .username("test2")
                .password("1234")
                .build();

//        given(repo.save(any())).willReturn(Client.class);
        this.mvc.perform(post(domain + "/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(client)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.username").value(client.getUsername()))
                .andDo(print());
    }

    @Test
    void authFailTest() throws Exception {
        this.mvc.perform(
                get("/access2")
        ).andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    void authenticationTest() throws Exception {
        // TODO DataIntegity Exception Handling
//        repo.save(client);
//        repo.save(client);

        LoginToken token = new LoginToken();
        ReflectionTestUtils.setField(token, "username", client.getUsername());
        ReflectionTestUtils.setField(token, "password", client.getPassword());
        AccessTokenContainer tokenContainer = service.login(token);


        this.mvc.perform(
                get("/access2")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenContainer.getAccess_token())
        )
                .andExpect(status().isOk())
                .andExpect(content().string("ok"))
                .andDo(print());
    }

    @Disabled
    @Test
    void logout() throws Exception {
        Client client = Client.builder().username("name").password("paass").build();
        this.repo.save(client);
        LoginToken token = new LoginToken();
        ReflectionTestUtils.setField(token, "username", client.getUsername());
        ReflectionTestUtils.setField(token, "password", client.getPassword());
        AccessTokenContainer tokenContainer = service.login(token);


        this.mvc.perform(
                get("/api/auth/logout")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenContainer.getAccess_token())
        )
                .andExpect(status().isOk())
                .andDo(print());

        this.mvc.perform(
                get("/access2")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenContainer.getAccess_token())
        ).andExpect(status().isForbidden())
                .andDo(print());
    }



    @Test
    void loginClient() throws Exception {
//        given(repo.save(client)).willReturn(client);

        // given

        assertNotNull(repo.save(client).getPassword());
        // when

        log.info("id {} ;", repo.save(client).getId());

        log.info("user : {} ", client.getId());
        this.mvc.perform(
                post(domain + "login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(client))
        ).andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$..access_token").exists())
                .andExpect(status().isOk())
                .andDo(print());
    }


}