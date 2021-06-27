package com.alconn.copang.utils;

import com.alconn.copang.client.Client;
import com.alconn.copang.client.ClientRepo;
import com.alconn.copang.client.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.stereotype.Component;

@Component
public class TestUtils {

    @Autowired
    ClientRepo repo;

    public Client generateRealClient() {
        return repo.save(
                Client.builder()
                        .username("coppang143")
                        .password("비밀번호123!")
                        .description("안녕하세요!")
                        .phone("010-0030-9090")
                        .realName("길동홍길동")
                        .role(Role.CLIENT)
                        .build()
        );
    }

}
