package com.alconn.copang.payment;

import com.alconn.copang.order.Orders;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ImpPaymentInfo {

    @Id @GeneratedValue
    private Long impPaymentId;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Orders orders;

    private Boolean success;

    private String imp_uid;

    private String pay_method;

    private String pg_provider;

    private String pg_tid;

    private String pg_id;

    private Boolean escrow;

//    private

}
