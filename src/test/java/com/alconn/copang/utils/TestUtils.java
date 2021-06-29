package com.alconn.copang.utils;

import com.alconn.copang.client.Client;
import com.alconn.copang.client.ClientRepo;
import com.alconn.copang.client.Role;
import com.alconn.copang.exceptions.InvalidTokenException;
import com.alconn.copang.security.provider.JwtTokenProvider;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.stereotype.Component;

@Component
public class TestUtils {

    public static Client client;
    @Autowired
    ClientRepo repo;

    @Autowired
    JwtTokenProvider provider;


    public Client generateRealClient() {
        return Client.builder()
            .username("coppang143")
            .password("비밀번호123!")
            .description("안녕하세요!")
            .phone("010-0030-9090")
            .realName("길동홍길동")
            .role(Role.CLIENT)
            .build();
    }

    public Client generateMockClient() {
        return Client.builder()
            .clientId(1L)
            .username("coppang143")
            .password("비밀번호123!")
            .description("안녕하세요!")
            .phone("010-0030-9090")
            .realName("길동홍길동")
            .role(Role.CLIENT)
            .build();
    }

    public String genToken(Client client) throws InvalidTokenException {
        return provider.createAccessToken(client).orElseThrow(InvalidTokenException::new);
    }

    public String genToken() throws InvalidTokenException {
        return genToken(generateMockClient());
    }

    public String genAuthHeader() throws InvalidTokenException {
        return "Bearer " + genToken();
    }

    public <T> JsonFieldType convert(Class<T> tClass) {
        if (tClass.isAssignableFrom(String.class)) {
            return JsonFieldType.STRING;
        }

        if (tClass.isAssignableFrom(Number.class)) {
            return JsonFieldType.NUMBER;
        }

        if(tClass.isAssignableFrom(Map.class)) {
            return JsonFieldType.OBJECT;
        }

        if(tClass.isAssignableFrom(List.class) || tClass.isAssignableFrom(Set.class)) {
            return JsonFieldType.ARRAY;
        }

        if (tClass.isAssignableFrom(Boolean.class)){
            return JsonFieldType.BOOLEAN;
        }

        return JsonFieldType.OBJECT;
    }
}
