package com.alconn.copang.payment;

import static org.junit.jupiter.api.Assertions.*;

import com.alconn.copang.exceptions.NoSuchEntityExceptions;
import com.alconn.copang.exceptions.ValidationException;
import javafx.scene.effect.Reflection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
class PaymentServiceTest {

    @Autowired
    PaymentService service;

    @Autowired
    RestTemplate restTemplate;

    @Test
    void name() {
//        String token = ReflectionTestUtils.invokeMethod(service, "getImpToken");
        String token = service.getImpToken();
        System.out.println("token = " + token);
    }

    @Test
    void validateTest() throws ValidationException, NoSuchEntityExceptions {

        service.validatePayment("imp_828634498901", 1L);
    }

    @Test
    void orderPay() {

    }
}