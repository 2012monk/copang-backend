package com.alconn.copang.payment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor @AllArgsConstructor
@Entity
public class ImpCancelInfo {

    @Id @GeneratedValue
    private Long impCancelInfoId;

    private String pg_tid;

    private Integer amount;

    private String reason;

    private String receipt_url;

    @ManyToOne(optional = false)
    @JoinColumn(name = "imp_payment_info_id")
    private ImpPaymentInfo impPaymentInfo;


}
