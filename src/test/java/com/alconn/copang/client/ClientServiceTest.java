package com.alconn.copang.client;

import com.alconn.copang.client.UserForm.Response;
import com.alconn.copang.exceptions.NoSuchUserException;
import com.alconn.copang.seller.Seller;
import com.alconn.copang.seller.SellerRepository;
import com.alconn.copang.utils.TestUtils;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ClientServiceTest {
    
    @Autowired
    ClientService service;

    @Autowired
    SellerRepository sellerRepository;
    @Autowired
    TestUtils utils;

    @Autowired
    EntityManager em;


    @Test
    void test123() {
        
        UserForm form = new UserForm();

        ReflectionTestUtils.setField(form, "username", "asdf");
        ReflectionTestUtils.setField(form, "password", "kljljk");
        
        Client client = service.register(form);
        System.out.println("client.getUsername() = " + client.getUsername());
        
    }

    @Disabled
    @Transactional
    @Test
    void getSeller() throws NoSuchUserException {
        Seller seller = utils.getSeller();

        sellerRepository.save(seller);

        em.flush();;
        em.clear();

        Response res = this.service.getSeller(seller.getClientId());

        assertNotNull(res);
        assertEquals(seller.getUsername(), res.getUsername());

    }
}