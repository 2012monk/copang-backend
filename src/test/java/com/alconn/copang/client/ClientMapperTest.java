package com.alconn.copang.client;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ClientMapperTest {


//    @Autowired
//    @Autowired
    private final ClientMapper mapper
        = Mappers.getMapper(ClientMapper.class);

    @Test
    void sdf() {
        Client client = Client.builder()
                .username("name")
                .password("pass")
                .role(Role.CLIENT)
                .build();

//        UserForm form = testMapper.f(client);
//        System.out.println("client = " + client.getUsername());
//        System.out.println("form.getUsername() = " + form.getUsername());

    }

    @Test
    void name() {
        Client client = Client.builder()
                .username("name")
                .password("pass")
                .role(Role.CLIENT)
                .build();
        
        UserForm form = mapper.c(client);
        System.out.println("client = " + client.getUsername());
        System.out.println("form.getUsername() = " + form.getUsername());
        assertEquals(form.getUsername(), client.getUsername());


    }
}