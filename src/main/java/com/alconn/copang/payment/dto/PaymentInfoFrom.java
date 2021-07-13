package com.alconn.copang.payment.dto;

import com.alconn.copang.payment.ImpCancelInfo;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class PaymentInfoFrom {

    private String imp_uid;

    private Integer amount;

    private Integer cancel_amount;

    private String pay_method;

    private String pg_provider;

    private String pg_tid;

    private String pg_id;

    private Boolean escrow;

    private String status;

    private List<ImpCancelInfo> cancel_history;

    private Long paid_at;

    private Long cancelled_at;

    private Long failed_at;

    private List<String> cancel_receipt_urls;

}
