package com.alconn.copang.address;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.alconn.copang.client.Client;
import com.alconn.copang.client.ClientRepo;
import com.alconn.copang.common.EntityPriority;
import com.alconn.copang.exceptions.NoSuchEntityExceptions;
import com.alconn.copang.exceptions.ValidationException;
import com.alconn.copang.utils.TestUtils;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
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
    void save() throws ValidationException, NoSuchEntityExceptions {
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

        AddressForm form1 = service.getPrimaryAddress(client.getClientId());
        assertNotNull(form1);
        assertEquals(form.getAddress(), form1.getAddress());
    }

    @DisplayName("기본주소로 저장된다")
    @Transactional
    @Test
    void saveDefault() throws NoSuchEntityExceptions, ValidationException {
        Client client = utils.generateRealClient();
        repo.save(client);

        AddressForm form =
            AddressForm.builder()
                .address("addr1")
                .detail("detail1")
                .preRequest("req")
                .build();

        AddressForm mid = service.registerAddress(form, client.getClientId());

        assertEquals(mid.getPriority(), EntityPriority.PRIMARY);
        m.flush();
        m.clear();

        AddressForm res = service.registerPrimaryAddress(form, client.getClientId());

        m.flush();
        m.clear();
        List<AddressForm> list = service.getAllAddresses(client.getClientId());

        assertEquals(list.size(), 2);

        AddressForm valid = list.stream().filter(a -> a.getPriority() == EntityPriority.PRIMARY)
            .findAny().orElseThrow(RuntimeException::new);

        int p = (int) list.stream().filter(a -> a.getPriority() == EntityPriority.PRIMARY).count();

        int s = (int) list.stream().filter(a -> a.getPriority() == EntityPriority.SECONDARY)
            .count();

        assertEquals(p, 1);
        assertEquals(s, 1);

        AddressForm f1 = list.get(0);
        AddressForm f2 = list.get(1);

        AddressForm v = f1.getAddressId().equals(res.getAddressId()) ? f1 : f2;

        assertEquals(res.getPriority(), EntityPriority.PRIMARY);


    }


    @Transactional
    @DisplayName("기본주소로 변경된다")
    @Test
    void setDefault() throws ValidationException, NoSuchEntityExceptions {
        Client client = utils.generateRealClient();
        repo.save(client);

        AddressForm form =
            AddressForm.builder()
                .address("addr1")
                .detail("detail1")
                .preRequest("req")
                .build();

        AddressForm f1 = service.registerAddress(form, client.getClientId());
        AddressForm f2 = service.registerAddress(form, client.getClientId());

        m.flush();
        m.clear();

        assert service.setPrimaryAddress(f2.getAddressId(), client.getClientId());

        m.flush();
        m.clear();

        AddressForm valid = service.getPrimaryAddress(client.getClientId());

        assertEquals(f2.getAddressId(), valid.getAddressId());
    }

    @Transactional
    @Test
    void addressCountTest() throws ValidationException {
        Client client = utils.generateRealClient();
        repo.save(client);

        AddressForm form =
            AddressForm.builder()
                .address("addr1")
                .detail("detail1")
                .preRequest("req")
                .build();
        for (int i = 0; i < 5; i++) {
            service.registerAddress(form, client.getClientId());
        }

        assertThrows(
            ValidationException.class, () ->
                service.registerAddress(form, client.getClientId())
        );
    }

    @Transactional
    @Test
    void updateAddress() throws ValidationException, NoSuchEntityExceptions {
        Client client = utils.generateRealClient();
        repo.save(client);

        AddressForm form =
            AddressForm.builder()
                .address("addr1")
                .detail("detail1")
                .preRequest("req")
                .build();

        AddressForm f = service.registerAddress(form, client.getClientId());

        m.flush();
        m.clear();

        ReflectionTestUtils.setField(form, "address", "c");

        AddressForm res = service.updateAddress(form, f.getAddressId(), client.getClientId());

        assertEquals(res.getAddress(), "c");

    }

    @DisplayName("주소삭제")
    @Transactional
    @Test
    void delete() throws ValidationException, NoSuchEntityExceptions {
        Client client = utils.generateRealClient();
        repo.save(client);

        AddressForm form =
            AddressForm.builder()
                .address("addr1")
                .detail("detail1")
                .preRequest("req")
                .build();
        AddressForm f = service.registerAddress(form, client.getClientId());

        m.flush();
        m.clear();

        assert service.deleteAddress(f.getAddressId(), client.getClientId()) != null;

        m.flush();
        m.clear();

        assert service.getAllAddresses(client.getClientId()).isEmpty();


    }

    @DisplayName("삭제와 함께 기본배송지 변경")
    @Transactional
    @Test
    void deleteWithPriority() throws ValidationException, NoSuchEntityExceptions {

        Client client = utils.generateRealClient();
        repo.save(client);

        AddressForm form =
            AddressForm.builder()
                .address("addr1")
                .detail("detail1")
                .preRequest("req")
                .build();
        List<AddressForm> res = new ArrayList<>(4);
        for (int i = 0; i < 4; i++) {
            res.add(service.registerAddress(form, client.getClientId()));
        }

        assert res.stream().filter(a -> a.getPriority() == EntityPriority.PRIMARY).count() == 1;

        AddressForm primary = res.stream().filter(a -> a.getPriority() == EntityPriority.PRIMARY)
            .findAny().orElseThrow(RuntimeException::new);

        m.flush();
        m.close();

        assert service.deleteAddress(primary.getAddressId(), client.getClientId()) != null;

        m.flush();
        m.close();

        List<AddressForm> list = service.getAllAddresses(client.getClientId());

        assert res.stream().filter(a -> a.getPriority() == EntityPriority.PRIMARY).count() == 1;

    }
}