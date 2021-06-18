package com.alconn.copang.client;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @InjectMocks
    private ClientService service;

    @Mock
    private ClientRepo repo;

    @DisplayName("Login 을 한다")
    @Test
    void login() {
    }

    @DisplayName("유저를 등록한다")
    @Test
    void signupClient() {
        Client client =
                Client.builder()
                .username("test")
                .password("1234")
                .role(Role.CLIENT)
                .build();

//        Client saved = repo.save(client);

//        given(repo.save(any())).willReturn(client);

    }
}