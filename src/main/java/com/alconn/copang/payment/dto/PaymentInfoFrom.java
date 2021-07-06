package com.alconn.copang.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class PaymentInfoFrom {

    private Boolean success;

    private String imp_uid;

    private Integer amount;

    private Integer cancel_amount;

    private String pay_method;

    private String pg_provider;

    private String pg_tid;

    private String pg_id;

    private Boolean escrow;

}
