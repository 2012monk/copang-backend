package com.alconn.copang.address;

import static org.junit.jupiter.api.Assertions.*;

import com.alconn.copang.client.Client;
import com.alconn.copang.client.ClientRepo;
import com.alconn.copang.exceptions.ValidationException;
import com.alconn.copang.utils.TestUtils;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class AddressServiceTest {

    @Autowired
    AddressService service;

    @Autowired
    ClientRepo repo;

    @Autowired
    EntityManager m;

    @Autowired
    TestUtils utils;


    @Transactional
    @Test
    void save() throws ValidationException {
        Client client = utils.generateRealClient();
        repo.save(client);
        Address address =
            Address.builder()
            .address("address1")
            .detail("deatila1")
            .preRequest("none")
            .build();

        AddressForm form =
            AddressForm.builder()
            .address("addr1")
            .detail("detail1")
            .preRequest("req")
            .build();

        service.registerAddress(form, client.getClientId());

        m.flush();
        m.clear();

        AddressForm form1 = service.getPrimaryAddress(1L);
        assertEquals(form.getAddress(), form1.getAddress());
    }

    @Test
    void get() {

    }
}