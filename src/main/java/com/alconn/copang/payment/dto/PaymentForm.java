package com.alconn.copang.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PaymentForm {

    @NoArgsConstructor @AllArgsConstructor
    @Builder
    @Getter
    public static class Request {
        String id;
    }

    @NoArgsConstructor @AllArgsConstructor
    @Builder
    @Getter
    public static class Response {
        String id;
    }


    public static class Prepare {

    }
}
