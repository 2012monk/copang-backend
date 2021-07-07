package com.alconn.copang.controller;

import com.alconn.copang.exceptions.NoSuchEntityExceptions;
import com.alconn.copang.exceptions.ValidationException;
import com.alconn.copang.payment.ImpPaymentInfo;
import com.alconn.copang.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PayTest {

    private final PaymentService service;

    @GetMapping("/pay/{uid}")
    public ImpPaymentInfo valid(@PathVariable String uid) throws NoSuchEntityExceptions, ValidationException {
        return service.validatePayment(uid, 2L);
    }

}
