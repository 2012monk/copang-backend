package com.alconn.copang.client;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ClientServiceTest {
    
    @Autowired
    ClientService service;



    @Test
    void test123() {
        
        UserForm form = new UserForm();

        ReflectionTestUtils.setField(form, "username", "asdf");
        ReflectionTestUtils.setField(form, "password", "kljljk");
        
        Client client = service.register(form);
        System.out.println("client.getUsername() = " + client.getUsername());
        
    }
}