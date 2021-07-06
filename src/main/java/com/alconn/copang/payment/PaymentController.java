package com.alconn.copang.payment;

import com.alconn.copang.annotations.InjectId;
import com.alconn.copang.common.ResponseMessage;
import com.alconn.copang.payment.dto.PaymentForm;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pay")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService service;

    @PostMapping
    public ResponseMessage<?> preparePayment(PaymentForm.Request request, @InjectId Long clientId){
        return ResponseMessage.success(service.preparePayment(request, clientId));
    }
}
