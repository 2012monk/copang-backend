package com.alconn.copang.payment;

import com.alconn.copang.payment.PaymentForm.Request;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class PaymentService {

    public PaymentForm.Prepare preparePayment(Request request, Long clientId) {
        return null;
    }


}
