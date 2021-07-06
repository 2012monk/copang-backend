package com.alconn.copang.payment;

import com.alconn.copang.order.Orders;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor @AllArgsConstructor

@Builder
@Getter
@Entity
public class ImpPaymentInfo {

    @Id @GeneratedValue
    private Long impPaymentId;

    @OneToOne(mappedBy = "impPaymentInfo")
    @JoinColumn(name = "order_id")
    private Orders orders;

    private Boolean success;

    private String imp_uid;

    private Integer amount;

    private String pay_method;

    private String pg_provider;

    private String pg_tid;

    private String pg_id;

    private Boolean escrow;


    public void setOrders(Orders orders) {
        this.orders = orders;
    }
}
