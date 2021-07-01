package com.alconn.copang.seller;

import static org.junit.jupiter.api.Assertions.*;

import com.alconn.copang.client.ClientRepo;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
class SellerTest {

    @Autowired
    ClientRepo repo;

    @Autowired
    EntityManager m;

    @Transactional
    @Test
    void name() {

        Seller seller =
            Seller.builder()
            .username("seller")
            .password("123444")
            .build();

        repo.save(seller);

        m.flush();
        m.clear();

        System.out.println("repo = " + ((Seller) repo.getById(seller.getClientId())).getUsername());
    }
}