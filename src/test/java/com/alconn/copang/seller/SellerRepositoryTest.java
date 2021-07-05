package com.alconn.copang.seller;

import static org.junit.jupiter.api.Assertions.*;

import com.alconn.copang.client.Client;
import com.alconn.copang.client.ClientRepo;
import com.alconn.copang.client.Role;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class SellerRepositoryTest {

    @Autowired
    SellerRepository sellerRepository;

    @Autowired
    ClientRepo repo;

    @Autowired
    EntityManager m;

    @Test
    void saveTest() {

        Seller seller =
            Seller.builder()
            .username("name")
            .password("gjlj")
            .role(Role.SELLER)
            .build();

        sellerRepository.save(seller);

        m.flush();
        m.clear();

        Client client = repo.findClientByUsername(seller.getUsername()).orElseThrow(RuntimeException::new);

        assertEquals(client.getUsername(), seller.getUsername());



    }
}