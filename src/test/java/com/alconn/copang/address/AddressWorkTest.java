package com.alconn.copang.address;

import static org.junit.jupiter.api.Assertions.*;

import com.alconn.copang.client.Client;
import com.alconn.copang.client.ClientRepo;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
//@ComponentScan(basePackageClasses = AddressMapper.class)
//@DataJpaTest
class AddressWorkTest {

    @Autowired
    AddressRepository repository;

    @Autowired
    AddressMapper mapper;

    @Autowired
    EntityManager m;

    @Autowired
    ClientRepo repo;


    @Disabled
    @Transactional
    @Test
    void registerAddress() {

        Client client = Client.builder()
            .clientId(1L)
            .username("hello")
            .password("14132")
            .phone("010101001")
            .build();

        repo.save(client);
        m.flush();
//        m.clear();
        Address address = Address.builder()
            .address("somthing")
            .client(client)
            .build();
        Address saved = repository.save(address);
        System.out.println("saved.getAddress1() = " + saved.getAddress());
        m.flush();
//        m.clear();

        Address get = repository.getById(address.getAddressId());
        assertNotNull(get);
        System.out.println("get.getAddress() = " + get.getAddress());
        assertNotNull(saved);
    }

    @Test
    void getAllAddresses() {
    }

    @Test
    void getPrimaryAddress() {
    }

    @Test
    void setPrimaryAddress() {
    }

    @Test
    void deleteAddress() {
    }

    @Test
    void updateAddress() {
    }
}