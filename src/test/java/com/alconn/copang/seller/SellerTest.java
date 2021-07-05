package com.alconn.copang.seller;

import static org.junit.jupiter.api.Assertions.*;

import com.alconn.copang.client.ClientRepo;
import com.alconn.copang.client.Role;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

@Disabled
@DataJpaTest
class SellerTest {

    @Autowired
    ClientRepo repo;

    @Autowired
    SellerRepository repository;

    @Autowired
    EntityManager m;

    @Transactional
    @Test
    void name() {

        Seller seller =
            Seller.builder()
            .username("seller")
            .password("123444")
                .role(Role.SELLER)
            .build();

        repo.save(seller);

        m.flush();
        m.clear();

        System.out.println("repo = " + (repo.getById(seller.getClientId())).getUsername());

    }
}