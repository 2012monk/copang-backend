package com.alconn.copang.jpa;

import com.alconn.copang.client.Client;
import com.alconn.copang.client.ClientRepo;
import com.alconn.copang.store.Store;
import com.alconn.copang.store.StoreRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;



@SpringBootTest
public class TimeTest {

    @Autowired
    ClientRepo repo;

    @Autowired
    StoreRepo storeRepo;

    @Test
    void name2() {
    }

//    @Transactional
    @Test
    void name() {
        System.out.println("storeRepo.save(Store.builder().build()).getCreatedTime() = " + storeRepo.save(Store.builder().build()).getCreatedTime());
        Client client = Client.builder().username("Adsf").password("asdf").build();
        System.out.println("repo.save(client).getSignInDate() = " + repo.save(client).getSignInDate());
        System.out.println("client.getId() = " + client.getId());
    }
}
